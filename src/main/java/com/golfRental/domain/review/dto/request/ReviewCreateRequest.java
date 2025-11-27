package com.golfRental.domain.review.dto.request;

import com.golfRental.domain.review.entity.Review;
import com.golfRental.domain.user.entity.User;
import jakarta.validation.constraints.*;

public record ReviewCreateRequest(
        @NotNull(message = "사용자 점수는 필수입니다.")
        @Min(value = 1, message = "사용자 점수는 1점 이상이어야 합니다.")
        @Max(value = 5, message = "사용자 점수는 5점 이하여야 합니다.")
        Integer userScore,

        @NotBlank(message = "리뷰 내용은 필수입니다.")
        @Size(max = 1000, message = "리뷰 내용은 1000자 이하여야 합니다.")
        String content
) {
    public Review toEntity(User user) {
        return Review.builder()
                .user(user)
                .userScore(userScore)
                .content(content)
                .build();
    }
}