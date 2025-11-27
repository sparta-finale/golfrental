package com.golfRental.domain.review.service.command;

import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;

public interface ReviewCommandService {

    ReviewResponse createReview(Long userId, ReviewCreateRequest request);
}