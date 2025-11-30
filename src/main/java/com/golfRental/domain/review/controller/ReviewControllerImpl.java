package com.golfRental.domain.review.controller;


import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewGetResponse;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.service.command.ReviewCommandService;
import com.golfRental.domain.review.service.query.ReviewQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewControllerImpl implements ReviewController {

    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    @Override
    @PostMapping
    public ResponseEntity<CommonApiResponse<ReviewResponse>> createReview(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody ReviewCreateRequest request
    ) {
        ReviewResponse response = reviewCommandService.createReview(authUser.getUserId(), request);
        return CommonApiResponse.created(response, "리뷰 생성 성공");
    }

    @Override
    @GetMapping("/{reviewId}")
    public ResponseEntity<CommonApiResponse<ReviewResponse>> getReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId
    ) {
        ReviewResponse response = reviewQueryService.getReview(reviewId);
        return CommonApiResponse.success(response, "리뷰 조회 성공");
    }

    @Override
    @GetMapping
    public ResponseEntity<CommonApiResponse<SliceResponse<ReviewGetResponse>>> getReviewsByTargetUser(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam Long targetUserId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SliceResponse<ReviewGetResponse> response = reviewQueryService.getReviewsByTargetUser(targetUserId, pageable);

        return CommonApiResponse.success(response, "리뷰 목록 조회 성공");
    }
}