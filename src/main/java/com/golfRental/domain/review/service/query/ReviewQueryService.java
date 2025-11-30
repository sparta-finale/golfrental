package com.golfRental.domain.review.service.query;

import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.entity.Review;
import org.springframework.stereotype.Service;

@Service
public interface ReviewQueryService {

    ReviewResponse getReview(Long reviewId);

    //내부용
    Review findById(Long reviewId);
}
