package com.golfRental.domain.review.service.command;

import com.golfRental.domain.notification.dto.request.NotificationCreateRequest;
import com.golfRental.domain.notification.enums.NotificationType;
import com.golfRental.domain.notification.service.command.NotificationCommandService;
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
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final ReservationQueryService reservationQueryService;
    private final UserQueryService userQueryService;
    private final NotificationCommandService notificationCommandService;

    @Override
    @Transactional
    public ReviewResponse createReview(Long currentUserId, ReviewCreateRequest request) {
        log.info("리뷰 생성 요청 - reservationId: {}, currentUserId: {}",
                request.reservationId(), currentUserId);

        // 1. 예약 조회
        Reservation reservation = reservationQueryService.findById(request.reservationId());

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

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                sendReviewNotification(user, targetUser, savedReview);
            }
        });

        return ReviewResponse.from(savedReview);
    }

    private void sendReviewNotification(User reviewer, User targetUser, Review review) {
        try {
            NotificationCreateRequest notificationRequest = NotificationCreateRequest.builder()
                    .receiverId(targetUser.getId())
                    .title("새로운 리뷰")
                    .content(reviewer.getUsername() + "님이 회원님에 대한 리뷰를 작성했습니다.")
                    .type(NotificationType.REVIEW_CREATED)
                    .referenceId(review.getId())
                    .build();

            notificationCommandService.createNotification(notificationRequest);

        } catch (Exception e) {
            log.error("리뷰 알림 전송 실패 - reviewId: {}, targetUserId: {}",
                    review.getId(), targetUser.getId(), e);
        }
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
        review.update(request.userScore(), request.content());

        return ReviewResponse.from(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long currentUserId, Long reviewId) {

        // 1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 2. 작성자 검증
        if (!review.isAuthor(currentUserId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_FORBIDDEN);
        }

        // 3. Soft Delete
        review.delete();
    }
}