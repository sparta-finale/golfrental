package com.golfRental.domain.review.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.review.dto.response.ReviewGetResponse;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.entity.Review;
import com.golfRental.domain.review.exception.ReviewErrorCode;
import com.golfRental.domain.review.exception.ReviewException;
import com.golfRental.domain.review.repository.ReviewRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final UserQueryService userQueryService;

    @Override
    public ReviewResponse getReview(Long reviewId) {
        log.info("리뷰 단건 조회 - reviewId: {}", reviewId);

        Review review = reviewRepository.findByIdWithDetails(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        return ReviewResponse.from(review);
    }

    @Override
    public SliceResponse<ReviewGetResponse> getReviewsByTargetUser(Long targetUserId, Pageable pageable) {

        // 1. 대상 유저 존재 확인
        User targetUser = userQueryService.findById(targetUserId);

        // 2. 리뷰 목록 조회
        Slice<Review> reviews = reviewRepository.findByTargetUser(targetUser, pageable);

        // 3. DTO 변환
        Slice<ReviewGetResponse> content = reviews.map(ReviewGetResponse::from);

        return SliceResponse.fromSlice(content);
    }

    @Override
    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }
}