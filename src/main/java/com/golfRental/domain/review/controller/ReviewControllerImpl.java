package com.golfRental.domain.review.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.service.command.ReviewCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewCommandController implements ReviewController{

    private final ReviewCommandService reviewCommandService;

    /**
     * 리뷰 생성
     * TODO: Spring Security 적용 후 @AuthenticationPrincipal로 currentUserId 자동 주입
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonApiResponse<ReviewResponse> createReview(
            @Valid @RequestBody ReviewCreateRequest request,
            @RequestHeader("X-User-Id") Long currentUserId  // 임시: 헤더로 사용자 ID 전달
    ) {
        log.info("POST /api/reviews - reservationId: {}, currentUserId: {}",
                request.getReservationId(), currentUserId);

        ReviewResponse response = reviewCommandService.createReview(request, currentUserId);

        return CommonApiResponse.success(response, "리뷰가 성공적으로 생성되었습니다.");
    }
}