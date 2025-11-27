package com.golfRental.domain.point.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "point_accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = true)
    private User user;

    @Column(nullable = false)
    private Long balance;

    @Builder
    private PointAccount(User user, Long balance) {
        this.user = user;
        this.balance = balance;
    }
}
