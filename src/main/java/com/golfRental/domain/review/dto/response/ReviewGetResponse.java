package com.golfRental.domain.review.dto.response;

import com.golfRental.domain.review.entity.Review;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewGetResponse(
        Long reviewId,
        Long userId,
        String username,
        Long reservationId,
        Integer userScore,
        String content,
        LocalDateTime createdAt
) {
    public static ReviewGetResponse from(Review review) {
        return ReviewGetResponse.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .username(review.getUser().getUsername())
                .reservationId(review.getReservation().getId())
                .userScore(review.getUserScore())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }
}