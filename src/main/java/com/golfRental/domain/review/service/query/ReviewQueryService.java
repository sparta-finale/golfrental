package com.golfRental.domain.review.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.review.dto.response.ReviewGetResponse;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ReviewQueryService {

    ReviewResponse getReview(Long reviewId);


    SliceResponse<ReviewGetResponse> getReviewsByTargetUser(Long targetUserId, Pageable pageable);

    //내부용
    Review findById(Long reviewId);
}
