package com.golfRental.domain.point.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.point.enums.PointTransactionType;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "point_transactions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PointTransactionType type;

    @Column(nullable = false)
    private Long balanceAfter;

    @Builder
    private PointTransaction(User user, Long amount, PointTransactionType type, Long balanceAfter) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.balanceAfter = balanceAfter;
    }

    public static PointTransaction create(
            User user,
            Long amount,
            PointTransactionType type,
            Long balanceAfter
    ) {
        return PointTransaction.builder()
                .user(user)
                .amount(amount)
                .type(type)
                .balanceAfter(balanceAfter)
                .build();
    }
}
