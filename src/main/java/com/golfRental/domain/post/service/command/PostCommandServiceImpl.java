package com.golfRental.domain.post.service.command;

import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.response.PostCreateResponse;
import com.golfRental.domain.post.dto.response.PostUpdateResponse;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.exception.PostErrorCode;
import com.golfRental.domain.post.exception.PostException;
import com.golfRental.domain.post.repository.PostRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final UserQueryService userQueryService;

    // 이후 추가적으로 이미지, 카테고리 들어가야 함
    @Override
    public PostCreateResponse createPost(Long userId, PostCreateRequest postCreateRequest) {
        User user = userQueryService.findById(userId);

        Post post = Post.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .methodOfReceive(postCreateRequest.getMethodOfReceive())
                .methodOfReturn(postCreateRequest.getMethodOfReturn())
                .price(postCreateRequest.getPrice())
                .deposit(postCreateRequest.getDeposit())
                .dailyRate(postCreateRequest.getDailyRate())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);

        return PostCreateResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .methodOfReceive(savedPost.getMethodOfReceive())
                .methodOfReturn(savedPost.getMethodOfReturn())
                .price(savedPost.getPrice())
                .deposit(savedPost.getDeposit())
                .dailyRate(savedPost.getDailyRate())
                .tradeStatus(savedPost.getTradeStatus())
                .userId(userId)
                .username(user.getUsername())
                .address(user.getAddress())
                .nickname(user.getNickname())
                .build();
    }

    @Override
    public PostUpdateResponse updatePost(Long userId, Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findByIdWithUser(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_INVALID_ID)
        );

        User user = userQueryService.findById(userId);

        if (!Objects.equals(post.getUser(), user)) {
            throw new PostException(PostErrorCode.POST_NOT_EQUAL_CREATOR);
        }

        post.update(postUpdateRequest);

        return PostUpdateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .methodOfReceive(post.getMethodOfReceive())
                .methodOfReturn(post.getMethodOfReturn())
                .price(post.getPrice())
                .deposit(post.getDeposit())
                .dailyRate(post.getDailyRate())
                .tradeStatus(post.getTradeStatus())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
                .address(post.getUser().getAddress())
                .nickname(post.getUser().getNickname())
                .build();
    }
}
