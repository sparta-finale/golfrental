package com.golfRental.domain.reservation.listener;

import com.golfRental.domain.notification.dto.request.NotificationCreateRequest;
import com.golfRental.domain.notification.enums.NotificationType;
import com.golfRental.domain.notification.service.command.NotificationCommandService;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.event.*;
import com.golfRental.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationNotificationListener {

    private final NotificationCommandService notificationCommandService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReservationCreated(ReservationCreatedEvent event) {
        Reservation reservation = event.getReservation();
        User receiver = reservation.getHostUser();
        User guest = reservation.getGuestUser();

        try {
            NotificationCreateRequest request = NotificationCreateRequest.of(
                    receiver.getId(),
                    "새로운 예약 요청",
                    String.format("%s님이 예약을 요청했습니다.", guest.getNickname()),
                    NotificationType.RESERVATION,
                    reservation.getId()
            );

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("예약 생성 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getHostUser().getId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReservationApproved(ReservationApprovedEvent event) {
        Reservation reservation = event.getReservation();
        try {
            NotificationCreateRequest request = NotificationCreateRequest.of(
                    reservation.getGuestUser().getId(),
                    "예약 승인",
                    "예약이 승인되었습니다.",
                    NotificationType.RESERVATION,
                    reservation.getId()
            );

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("예약 승인 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getGuestUser().getId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReservationRejected(ReservationRejectedEvent event) {
        Reservation reservation = event.getReservation();
        try {
            NotificationCreateRequest request = NotificationCreateRequest.of(
                    reservation.getGuestUser().getId(),
                    "예약 거절",
                    "예약이 거절되었습니다.",
                    NotificationType.RESERVATION,
                    reservation.getId()
            );

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("예약 거절 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getGuestUser().getId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReservationCancelled(ReservationCancelledEvent event) {
        Reservation reservation = event.getReservation();
        try {
            NotificationCreateRequest request = NotificationCreateRequest.of(
                    reservation.getHostUser().getId(),
                    "예약 취소",
                    String.format("%s님이 예약을 취소했습니다.", reservation.getGuestUser().getNickname()),
                    NotificationType.RESERVATION,
                    reservation.getId()
            );

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("예약 취소 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getHostUser().getId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleRentalStarted(RentalStartedEvent event) {
        Reservation reservation = event.getReservation();
        try {
            NotificationCreateRequest request = NotificationCreateRequest.of(
                    reservation.getGuestUser().getId(),
                    "대여 시작",
                    "대여가 시작되었습니다. 물품을 사용하실 수 있습니다.",
                    NotificationType.RESERVATION,
                    reservation.getId()
            );

            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("대여 시작 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getGuestUser().getId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReturnRequested(ReturnRequestedEvent event) {
        Reservation reservation = event.getReservation();
        try {
            NotificationCreateRequest request = NotificationCreateRequest.of(
                    reservation.getHostUser().getId(),
                    "반납 요청",
                    String.format("%s님이 반납을 요청했습니다.", reservation.getGuestUser().getNickname()),
                    NotificationType.RESERVATION,
                    reservation.getId()
            );
            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("반납 요청 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getHostUser().getId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReturnCompleted(ReturnCompletedEvent event) {
        Reservation reservation = event.getReservation();
        try {
            NotificationCreateRequest request = NotificationCreateRequest.of(
                    reservation.getGuestUser().getId(),
                    "반납 완료",
                    "반납이 완료되었습니다. 이용해 주셔서 감사합니다.",
                    NotificationType.RESERVATION,
                    reservation.getId()
            );
            notificationCommandService.createNotification(request);

        } catch (Exception e) {
            log.error("반납 완료 알림 전송 실패 - reservationId: {}, receiverId: {}",
                    reservation.getId(), reservation.getGuestUser().getId(), e);
        }
    }
}
