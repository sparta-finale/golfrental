package com.golfRental.domain.reservation.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.reservation.enums.ReservationStatus;
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

// Post 연관관계는 이후 feature 단계에서 추가 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id", nullable = false)
//    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_user_id", nullable = false)
    private User guestUser;

    @Builder
    public Reservation(LocalDateTime reservationStartAt, LocalDateTime reservationEndAt, Integer price, Integer deposit, ReservationStatus status, User hostUser, User guestUser) {
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.price = price;
        this.deposit = deposit;
        this.status = status;
        this.hostUser = hostUser;
        this.guestUser = guestUser;
    }
}
