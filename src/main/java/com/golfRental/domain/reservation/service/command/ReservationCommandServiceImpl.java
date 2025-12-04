package com.golfRental.domain.reservation.service.command;

import com.golfRental.domain.notification.dto.request.NotificationCreateRequest;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import com.golfRental.domain.notification.enums.NotificationType;
import com.golfRental.domain.notification.service.command.NotificationCommandService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final PostQueryService postQueryService;
    private final UserQueryService userQueryService;
    private final NotificationCommandService notificationCommandService;

    @Autowired
    @Lazy
    private ReservationCommandServiceImpl self;

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

        // 트랜잭션 커밋 후 알림 전송
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                self.sendReservationCreatedNotification(saved);
            }
        });

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

        log.info("예약 승인 시작 - reservationId: {}, hostUserId: {}, guestUserId: {}",
                reservationId, reservation.getHostUser().getId(), reservation.getGuestUser().getId());

        reservation.approve(); // 엔티티 도메인 규칙 실행

        // 트랜잭션 커밋 후 알림 전송
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                log.info("트랜잭션 커밋 완료 - 알림 전송 시작");
                self.sendReservationApprovedNotification(reservation);
            }
        });

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

        // 트랜잭션 커밋 후 알림 전송
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                self.sendReservationRejectedNotification(reservation);
            }
        });

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

        // 트랜잭션 커밋 후 알림 전송
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                self.sendReservationCancelledNotification(reservation);
            }
        });

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

        // 트랜잭션 커밋 후 알림 전송
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                self.sendRentalStartedNotification(reservation);
            }
        });

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

        // 트랜잭션 커밋 후 알림 전송
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                self.sendReturnRequestedNotification(reservation);
            }
        });

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

        // 트랜잭션 커밋 후 알림 전송
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                self.sendReturnCompletedNotification(reservation);
            }
        });

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
        if (!post.isReservable()) {
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

    // 알림 전송 메서드들 (트랜잭션 커밋 후 실행)

    /**
     * 예약 생성 알림 - Host에게 전송
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendReservationCreatedNotification(Reservation reservation) {
        try {
            NotificationCreateRequest request = NotificationCreateRequest.builder()
                    .receiverId(reservation.getHostUser().getId())
                    .title("새로운 예약 요청")
                    .content(String.format("%s님이 예약을 요청했습니다.",
                            reservation.getGuestUser().getNickname()))
                    .type(NotificationType.RESERVATION)
                    .referenceId(reservation.getId())
                    .build();

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("예약 생성 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getHostUser().getId(), e);
        }
    }

    /**
     * 예약 승인 알림 - Guest에게 전송
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendReservationApprovedNotification(Reservation reservation) {
        try {
            log.info("예약 승인 알림 생성 시작 - reservationId: {}, guestUserId: {}",
                    reservation.getId(), reservation.getGuestUser().getId());

            NotificationCreateRequest request = NotificationCreateRequest.builder()
                    .receiverId(reservation.getGuestUser().getId())
                    .title("예약 승인")
                    .content("예약이 승인되었습니다.")
                    .type(NotificationType.RESERVATION)
                    .referenceId(reservation.getId())
                    .build();

            NotificationResponse response = notificationCommandService.createNotification(request);

            log.info("예약 승인 알림 생성 완료 - notificationId: {}, receiverId: {}",
                    response.id(), response.receiverId());

        } catch (Exception e) {
            log.error("예약 승인 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getGuestUser().getId(), e);
        }
    }

    /**
     * 예약 거절 알림 - Guest에게 전송
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendReservationRejectedNotification(Reservation reservation) {
        try {
            NotificationCreateRequest request = NotificationCreateRequest.builder()
                    .receiverId(reservation.getGuestUser().getId())
                    .title("예약 거절")
                    .content("예약이 거절되었습니다.")
                    .type(NotificationType.RESERVATION)
                    .referenceId(reservation.getId())
                    .build();

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("예약 거절 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getGuestUser().getId(), e);
        }
    }

    /**
     * 예약 취소 알림 - Host에게 전송
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendReservationCancelledNotification(Reservation reservation) {
        try {
            NotificationCreateRequest request = NotificationCreateRequest.builder()
                    .receiverId(reservation.getHostUser().getId())
                    .title("예약 취소")
                    .content(String.format("%s님이 예약을 취소했습니다.",
                            reservation.getGuestUser().getNickname()))
                    .type(NotificationType.RESERVATION)
                    .referenceId(reservation.getId())
                    .build();

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("예약 취소 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getHostUser().getId(), e);
        }
    }

    /**
     * 대여 시작 알림 - Guest에게 전송
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendRentalStartedNotification(Reservation reservation) {
        try {
            NotificationCreateRequest request = NotificationCreateRequest.builder()
                    .receiverId(reservation.getGuestUser().getId())
                    .title("대여 시작")
                    .content("대여가 시작되었습니다. 물품을 사용하실 수 있습니다.")
                    .type(NotificationType.RESERVATION)
                    .referenceId(reservation.getId())
                    .build();

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("대여 시작 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getGuestUser().getId(), e);
        }
    }

    /**
     * 반납 요청 알림 - Host에게 전송
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendReturnRequestedNotification(Reservation reservation) {
        try {
            NotificationCreateRequest request = NotificationCreateRequest.builder()
                    .receiverId(reservation.getHostUser().getId())
                    .title("반납 요청")
                    .content(String.format("%s님이 반납을 요청했습니다.",
                            reservation.getGuestUser().getNickname()))
                    .type(NotificationType.RESERVATION)
                    .referenceId(reservation.getId())
                    .build();

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("반납 요청 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getHostUser().getId(), e);
        }
    }

    /**
     * 반납 완료 알림 - Guest에게 전송
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendReturnCompletedNotification(Reservation reservation) {
        try {
            NotificationCreateRequest request = NotificationCreateRequest.builder()
                    .receiverId(reservation.getGuestUser().getId())
                    .title("반납 완료")
                    .content("반납이 완료되었습니다. 이용해 주셔서 감사합니다.")
                    .type(NotificationType.RESERVATION)
                    .referenceId(reservation.getId())
                    .build();

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("반납 완료 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getGuestUser().getId(), e);
        }
    }
}