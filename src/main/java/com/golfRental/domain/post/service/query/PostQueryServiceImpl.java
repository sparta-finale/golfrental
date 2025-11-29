package com.golfRental.domain.post.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.post.dto.response.PostGetAllResponse;
import com.golfRental.domain.post.dto.response.PostGetMyResponse;
import com.golfRental.domain.post.dto.response.PostGetsResponse;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.exception.PostErrorCode;
import com.golfRental.domain.post.exception.PostException;
import com.golfRental.domain.post.repository.PostRepository;
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
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final UserQueryService userQueryService;

    @Override
    public SliceResponse<PostGetAllResponse> getAll(Pageable pageable) {
        Slice<Post> posts = postRepository.findAllOrderByStatus(pageable);

        Slice<PostGetAllResponse> content = posts.map(post -> PostGetAllResponse.builder()
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
                .categoryId(post.getCategory().getId())
                .categoryName(post.getCategory().getName())
                .build());

        return SliceResponse.fromSlice(content);
    }

    @Override
    public PostGetsResponse getPost(Long postId) {
        Post post = findById(postId);

        return PostGetsResponse.builder()
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
                .categoryId(post.getCategory().getId())
                .categoryName(post.getCategory().getName())
                .build();
    }

    @Override
    public SliceResponse<PostGetMyResponse> getMyPost(Long userId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Slice<Post> posts = postRepository.findAllByUserOrderByStatus(user, pageable);

        Slice<PostGetMyResponse> contents = posts.map(post -> PostGetMyResponse.builder()
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
                .categoryId(post.getCategory().getId())
                .categoryName(post.getCategory().getName())
                .build());

        return SliceResponse.fromSlice(contents);
    }

    @Override
    public Post findById(Long postId) {
        return postRepository.findByIdWithUserAndCategory(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_INVALID_ID)
        );
    }
}
