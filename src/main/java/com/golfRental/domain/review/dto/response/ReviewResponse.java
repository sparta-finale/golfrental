package com.golfRental.domain.review.dto.response;

import com.golfRental.domain.review.entity.Review;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewResponse(
        Long reviewId,
        Long userId,
        String username,
        Long targetUserId,
        String targetUsername,
        Long reservationId,
        Integer userScore,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .username(review.getUser().getUsername())
                .targetUserId(review.getTargetUser().getId())
                .targetUsername(review.getTargetUser().getUsername())
                .reservationId(review.getReservation().getId())
                .userScore(review.getUserScore())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}