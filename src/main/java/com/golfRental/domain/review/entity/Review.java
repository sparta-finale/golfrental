package com.golfRental.domain.review.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 평가하는 유저 (작성자)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;  // 평가받는 유저

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    @Column(name = "user_score", nullable = false)
    private Integer userScore;

    @Column(nullable = false, length = 1000)
    private String content;

    @Builder
    private Review(User user, User targetUser, Reservation reservation, Integer userScore, String content) {
        this.user = user;
        this.targetUser = targetUser;
        this.reservation = reservation;
        this.userScore = userScore;
        this.content = content;
    }
    
    public void update(Integer userScore, String content) {
        this.userScore = userScore;
        this.content = content;
    }
}