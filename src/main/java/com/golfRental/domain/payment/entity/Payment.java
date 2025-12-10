package com.golfRental.domain.payment.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.payment.enums.PaymentStatus;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String paymentKey;

    @Column(nullable = false, unique = true, length = 100)
    private String orderId;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Payment(String paymentKey,
                    String orderId,
                    Long amount,
                    PaymentStatus status,
                    User user
    ) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.user = user;
    }

    public static Payment createSuccess(String paymentKey, String orderId, Long amount, User user) {
        return new Payment(paymentKey, orderId, amount, PaymentStatus.SUCCESS, user);
    }
}