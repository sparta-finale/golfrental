package com.golfRental.domain.reservation.service.command;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.service.query.PostQueryService;
import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;
import com.golfRental.domain.reservation.dto.response.ReservationUpdateStatusResponse;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.enums.ReservationStatus;
import com.golfRental.domain.reservation.exception.ReservationErrorCode;
import com.golfRental.domain.reservation.exception.ReservationException;
import com.golfRental.domain.reservation.repository.ReservationRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final PostQueryService postQueryService;
    private final UserQueryService userQueryService;

    // 예약 생성
    @Override
    public ReservationCreateResponse createReservation(ReservationCreateRequest request, Long userId) {

        Post post = postQueryService.findById(request.getPostId());
        User guestUser = userQueryService.findById(userId);
        User hostUser = post.getUser();

        // 검증 로직 시작
        validateReservationCreation(post, guestUser, request);

        Reservation reservation = Reservation.builder()
                .post(post)
                .hostUser(hostUser)
                .guestUser(guestUser)
                .reservationStartAt(request.getReservationStartAt())
                .reservationEndAt(request.getReservationEndAt())
                .price(request.getPrice())
                .deposit(request.getDeposit())
                .status(ReservationStatus.REQUESTED)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        return ReservationCreateResponse.builder()
                .reservationId(saved.getId())
                .postId(saved.getPost().getId())
                .hostUserId(saved.getHostUser().getId())
                .guestUserId(saved.getGuestUser().getId())
                .reservationStartAt(saved.getReservationStartAt())
                .reservationEndAt(saved.getReservationEndAt())
                .price(saved.getPrice())
                .deposit(saved.getDeposit())
                .status(saved.getStatus())
                .build();
    }

    // 승인
    @Override
    public ReservationUpdateStatusResponse approveReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyHost(reservationId, userId);

        reservation.approve(); // 엔티티 도메인 규칙 실행

        return ReservationUpdateStatusResponse.builder()
                .reservationId(reservation.getId())
                .status(reservation.getStatus())
                .build();
    }

    // 거절
    @Override
    public ReservationUpdateStatusResponse rejectReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyHost(reservationId, userId);

        reservation.reject(); // 엔티티 도메인 규칙 실행

        return ReservationUpdateStatusResponse.builder()
                .reservationId(reservation.getId())
                .status(reservation.getStatus())
                .build();
    }

    // 취소
    @Override
    public ReservationUpdateStatusResponse cancelReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyGuest(reservationId, userId);

        reservation.cancel(); // 엔티티 도메인 규칙 실행

        return ReservationUpdateStatusResponse.builder()
                .reservationId(reservation.getId())
                .status(reservation.getStatus())
                .build();
    }

    // 대여 시작
    @Override
    public ReservationUpdateStatusResponse startReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyHost(reservationId, userId);

        reservation.startRental(); // 엔티티 도메인 규칙 실행

        return ReservationUpdateStatusResponse.builder()
                .reservationId(reservation.getId())
                .status(reservation.getStatus())
                .build();
    }

    // 반납 요청
    @Override
    public ReservationUpdateStatusResponse requestReturn(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyGuest(reservationId, userId);

        reservation.requestReturn(); // 엔티티 도메인 규칙 실행

        return ReservationUpdateStatusResponse.builder()
                .reservationId(reservation.getId())
                .status(reservation.getStatus())
                .build();
    }

    // 반납 승인(완료)
    @Override
    public ReservationUpdateStatusResponse completeReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyHost(reservationId, userId);

        reservation.complete(); // 엔티티 도메인 규칙 실행


        return ReservationUpdateStatusResponse.builder()
                .reservationId(reservation.getId())
                .status(reservation.getStatus())
                .build();
    }

    // 공통 조회 메서드
    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() ->
                        new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND)
                );
    }

    // 호스트 검증
    private Reservation findReservationAndVerifyHost(Long reservationId, Long userId) {

        Reservation reservation = findReservationById(reservationId);

        if (!reservation.getHostUser().getId().equals(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_FORBIDDEN);
        }

        return reservation;
    }

    // 게스트 검증
    private Reservation findReservationAndVerifyGuest(Long reservationId, Long userId) {

        Reservation reservation = findReservationById(reservationId);

        if (!reservation.getGuestUser().getId().equals(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_FORBIDDEN);
        }

        return reservation;
    }

    // 예약 생성 전 검증 수행
    private void validateReservationCreation(Post post, User guestUser, ReservationCreateRequest request) {

        // 자기 자신의 게시글 예약 방지
        if (post.isOwnedBy(guestUser)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_SELF_BOOKING_NOT_ALLOWED);
        }

        // 예약 날짜 유효성 (시작일 < 종료일)
        if (!request.getReservationStartAt().isBefore(request.getReservationEndAt())) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_INVALID_DATE_RANGE);
        }

        // Post 상태 확인 (BEFORE_TRANSACTION 또는 TRADING만 허용)
        if (post.isReservable()) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_POST_NOT_AVAILABLE);
        }

        // 중복 예약 방지
        boolean hasConflict = reservationRepository.existsConflictingReservation(
                post.getId(),
                request.getReservationStartAt(),
                request.getReservationEndAt()
        );

        if (hasConflict) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_DATE_CONFLICT);
        }
    }
}