package com.golfRental.domain.review.service.query;

import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.entity.Review;
import com.golfRental.domain.review.exception.ReviewErrorCode;
import com.golfRental.domain.review.exception.ReviewException;
import com.golfRental.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {

    ReviewRepository reviewRepository;

    @Override
    public ReviewResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        return ReviewResponse.from(review);
    }

    @Override
    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }
}
