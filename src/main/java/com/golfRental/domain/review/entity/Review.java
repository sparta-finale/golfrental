package com.golfRental.domain.review.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    private User user;

    @Min(value = 1, message = "점수는 1점 이상이어야 합니다.")
    @Max(value = 5, message = "점수는 5점 이하여야 합니다.")
    @Column(name = "user_score", nullable = false)
    private Integer userScore;

    @Column(nullable = false, length = 1000)
    private String content;

    @Builder
    private Review(User user, Integer userScore, String content) {
        this.user = user;
        this.userScore = userScore;
        this.content = content;
    }
}