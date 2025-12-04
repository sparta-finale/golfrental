package com.golfRental.domain.point.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.point.exception.PointErrorCode;
import com.golfRental.domain.point.exception.PointException;
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
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private Long balance;

    @Version
    private Long version;

    @Builder
    private PointAccount(User user, Long balance) {
        this.user = user;
        this.balance = balance;
    }

    // 포인트 계좌 생성
    public static PointAccount createDefault(User user) {
        return PointAccount.builder()
                .user(user)
                .balance(0L)
                .build();
    }

    // 포인트 사용 도메인
    public void use(Long amount) {
        if (this.balance < amount) {
            throw new PointException(PointErrorCode.POINT_NOT_ENOUGH);
        }
        this.balance -= amount;
    }
}
