package com.golfRental.domain.review.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.request.ReviewUpdateRequest;
import com.golfRental.domain.review.dto.response.ReviewGetResponse;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import org.springframework.data.domain.Pageable;
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

    ResponseEntity<CommonApiResponse<SliceResponse<ReviewGetResponse>>> getReviewsByTargetUser(
            AuthUser authUser,
            Long targetUserId,
            Pageable pageable
    );

    ResponseEntity<CommonApiResponse<ReviewResponse>> updateReview(
            AuthUser authUser,
            Long reviewId,
            ReviewUpdateRequest request
    );

    ResponseEntity<CommonApiResponse<Void>> deleteReview(
            AuthUser authUser,
            Long reviewId
    );
}