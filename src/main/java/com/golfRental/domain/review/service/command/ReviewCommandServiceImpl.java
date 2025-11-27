package com.golfRental.domain.review.service.command;

import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import com.golfRental.domain.review.entity.Review;
import com.golfRental.domain.review.repository.ReviewRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.command.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final UserCommandService userCommandService;

    @Override
    public ReviewResponse createReview(Long userId, ReviewCreateRequest request) {
        User user = userCommandService.findById(userId);
        Review review = request.toEntity(user);
        Review savedReview = reviewRepository.save(review);
        return ReviewResponse.from(savedReview);
    }
}