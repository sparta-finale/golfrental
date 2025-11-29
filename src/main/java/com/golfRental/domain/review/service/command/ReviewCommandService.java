package com.golfRental.domain.review.service.command;

import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;

public interface ReviewCommandService {

    //리뷰 생성
    ReviewResponse createReview(Long currentUserId, ReviewCreateRequest request);
}