package com.golfRental.domain.reservation.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.reservation.enums.ReservationStatus;
import com.golfRental.domain.reservation.exception.ReservationErrorCode;
import com.golfRental.domain.reservation.exception.ReservationException;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime reservationStartAt;

    @Column(nullable = false)
    private LocalDateTime reservationEndAt;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer deposit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_user_id", nullable = false)
    private User guestUser;

    @Builder
    private Reservation(
            LocalDateTime reservationStartAt,
            LocalDateTime reservationEndAt,
            Integer price,
            Integer deposit,
            ReservationStatus status,
            Post post,
            User hostUser,
            User guestUser
    ) {
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.price = price;
        this.deposit = deposit;
        this.status = status;
        this.post = post;
        this.hostUser = hostUser;
        this.guestUser = guestUser;
    }

    // 예약 승인
    public void approve() {

        if (this.status == ReservationStatus.APPROVED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_APPROVED);
        }

        if (this.status != ReservationStatus.REQUESTED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_CANNOT_APPROVE);
        }

        this.status = ReservationStatus.APPROVED;
    }

    // 예약 거절
    public void reject() {

        if (this.status != ReservationStatus.REQUESTED) {

            if (this.status == ReservationStatus.REJECTED) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_REJECTED);
            }

            throw new ReservationException(ReservationErrorCode.RESERVATION_CANNOT_REJECT);
        }

        this.status = ReservationStatus.REJECTED;
    }

    // 예약 취소
    public void cancel() {

        if (this.status == ReservationStatus.CANCELLED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_CANCELLED);
        }

        if (this.status != ReservationStatus.REQUESTED &&
                this.status != ReservationStatus.APPROVED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_CANNOT_CANCEL);
        }

        this.status = ReservationStatus.CANCELLED;
    }

    // 대여 시작
    public void startRental() {

        if (this.status == ReservationStatus.RENTED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_RENTED);
        }

        if (this.status != ReservationStatus.APPROVED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_CANNOT_START);
        }

        this.status = ReservationStatus.RENTED;
    }

    // 반납 요청
    public void requestReturn() {

        if (this.status == ReservationStatus.RETURNING) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_RETURNING);
        }

        if (this.status != ReservationStatus.RENTED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_CANNOT_REQUEST_RETURN);
        }

        this.status = ReservationStatus.RETURNING;
    }
}