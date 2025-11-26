package com.golfRental.domain.review.service.command;

import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReviewCommandService {

    ReviewResponse createReview(ReviewCreateRequest request);
}