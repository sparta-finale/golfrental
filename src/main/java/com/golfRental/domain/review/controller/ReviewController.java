package com.golfRental.domain.review.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import org.springframework.http.ResponseEntity;

public interface ReviewController {

    //리뷰 생성
    ResponseEntity<CommonApiResponse<ReviewResponse>> createReview(
            Long reservationId,
            ReviewCreateRequest request
    );
}
