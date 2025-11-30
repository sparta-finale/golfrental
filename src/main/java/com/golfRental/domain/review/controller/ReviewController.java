package com.golfRental.domain.review.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import org.springframework.http.ResponseEntity;

public interface ReviewController {

    ResponseEntity<CommonApiResponse<ReviewResponse>> createReview(
            AuthUser authUser,
            ReviewCreateRequest request
    );

    ResponseEntity<CommonApiResponse<ReviewResponse>> getReview(
            AuthUser authUser,
            Long reviewId
    );
}