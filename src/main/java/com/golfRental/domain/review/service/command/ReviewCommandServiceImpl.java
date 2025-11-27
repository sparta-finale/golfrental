package com.golfRental.domain.review.service.command;

import com.golfRental.common.exception.CommonErrorCode;
import com.golfRental.common.exception.GlobalException;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.entity.Review;
import com.golfRental.domain.review.repository.ReviewRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse createReview(ReviewCreateRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new GlobalException(CommonErrorCode.INVALID_INPUT_VALUE));

        Review review = request.toEntity(user);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.from(savedReview);
    }
}