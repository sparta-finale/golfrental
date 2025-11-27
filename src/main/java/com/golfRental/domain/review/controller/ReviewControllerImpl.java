package com.golfRental.domain.review.controller;


import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.service.command.ReviewCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewControllerImpl implements ReviewController {

    private final ReviewCommandService reviewCommandService;

    //리뷰 생성
    @PostMapping("/{reservationId}")
    public ResponseEntity<CommonApiResponse<ReviewResponse>> createReview(
            @PathVariable Long reservationId,
            @Valid @RequestBody ReviewCreateRequest request
    ) {
        ReviewResponse response = reviewCommandService.createReview(request);
        return CommonApiResponse.created(response, "리뷰 생성 성공");
    }
}
