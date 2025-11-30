package com.golfRental.domain.review.service.command;

import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.enums.ReservationStatus;
import com.golfRental.domain.reservation.service.query.ReservationQueryService;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.request.ReviewUpdateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.entity.Review;
import com.golfRental.domain.review.exception.ReviewErrorCode;
import com.golfRental.domain.review.exception.ReviewException;
import com.golfRental.domain.review.repository.ReviewRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final ReservationQueryService reservationQueryService;
    private final UserQueryService userQueryService;

    @Override
    @Transactional
    public ReviewResponse createReview(Long currentUserId, ReviewCreateRequest request) {
        log.info("리뷰 생성 요청 - reservationId: {}, currentUserId: {}",
                request.getReservationId(), currentUserId);

        // 1. 예약 조회
        Reservation reservation = reservationQueryService.findById(request.getReservationId());

        // 2. 예약 상태 검증 (COMPLETED 상태만 리뷰 작성 가능)
        validateReservationStatus(reservation);

        // 3. 리뷰 작성 권한 검증 (예약의 hostUser 또는 guestUser만 가능)
        validateReviewAuthor(reservation, currentUserId);

        // 4. 중복 리뷰 검증
        validateDuplicateReview(reservation.getId());

        // 5. 사용자 조회
        User user = userQueryService.findById(currentUserId);

        // 6. 평가받는 유저 결정 (상대방)
        User targetUser = determineTargetUser(reservation, currentUserId);

        // 7. 리뷰 생성
        Review review = request.toEntity(user, targetUser, reservation);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.from(savedReview);
    }

    private void validateReservationStatus(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            log.warn("예약 상태가 COMPLETED가 아님 - reservationId: {}, status: {}",
                    reservation.getId(), reservation.getStatus());
            throw new ReviewException(ReviewErrorCode.RESERVATION_NOT_COMPLETED);
        }
    }

    private void validateReviewAuthor(Reservation reservation, Long currentUserId) {
        boolean isHost = reservation.getHostUser().getId().equals(currentUserId);
        boolean isGuest = reservation.getGuestUser().getId().equals(currentUserId);

        if (!isHost && !isGuest) {
            log.warn("리뷰 작성 권한 없음 - reservationId: {}, currentUserId: {}",
                    reservation.getId(), currentUserId);
            throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_AUTHOR);
        }
    }

    private void validateDuplicateReview(Long reservationId) {
        if (reviewRepository.existsByReservationId(reservationId)) {
            log.warn("이미 리뷰가 존재함 - reservationId: {}", reservationId);
            throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
    }

    /**
     * 평가받는 유저 결정 (작성자의 상대방)
     */
    private User determineTargetUser(Reservation reservation, Long currentUserId) {
        if (reservation.getHostUser().getId().equals(currentUserId)) {
            // 호스트가 작성 → 게스트 평가
            return reservation.getGuestUser();
        } else {
            // 게스트가 작성 → 호스트 평가
            return reservation.getHostUser();
        }
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Long currentUserId, Long reviewId, ReviewUpdateRequest request) {

        // 1. 리뷰 조회
        Review review = reviewRepository.findByIdWithDetails(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 2. 작성자 검증
        if (!review.isAuthor(currentUserId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_FORBIDDEN);
        }

        // 3. 리뷰 수정
        review.update(request.getUserScore(), request.getContent());

        return ReviewResponse.from(review);
    }
}