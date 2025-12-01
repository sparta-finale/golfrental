package com.golfRental.domain.post.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.category.entity.Category;
import com.golfRental.domain.category.service.query.CategoryQueryService;
import com.golfRental.domain.post.dto.response.PostGetAllResponse;
import com.golfRental.domain.post.dto.response.PostGetByCategoryResponse;
import com.golfRental.domain.post.dto.response.PostGetMyResponse;
import com.golfRental.domain.post.dto.response.PostGetsResponse;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.exception.PostErrorCode;
import com.golfRental.domain.post.exception.PostException;
import com.golfRental.domain.post.repository.PostFavoritesRepository;
import com.golfRental.domain.post.repository.PostRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final PostFavoritesRepository postFavoritesRepository;
    private final UserQueryService userQueryService;
    private final CategoryQueryService categoryQueryService;

    @Override
    public SliceResponse<PostGetAllResponse> getAll(Long userId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Set<Long> favoritePostIds = postFavoritesRepository.findByUser(user).stream()
                .map(pf -> pf.getPost().getId())
                .collect(Collectors.toSet());

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
                .favorites(favoritePostIds.contains(post.getId()))
                .build());

        return SliceResponse.fromSlice(content);
    }

    @Override
    public PostGetsResponse getPost(Long userId, Long postId) {
        User user = userQueryService.findById(userId);

        Post post = findById(postId);

        boolean postFavorites = postFavoritesRepository.existsByUserAndPost(user, post);

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
                .favorites(postFavorites)
                .build();
    }

    @Override
    public SliceResponse<PostGetMyResponse> getMyPost(Long userId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Set<Long> favoritePostIds = postFavoritesRepository.findByUser(user).stream()
                .map(pf -> pf.getPost().getId())
                .collect(Collectors.toSet());

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
                .favorites(favoritePostIds.contains(post.getId()))
                .build());

        return SliceResponse.fromSlice(contents);
    }

    @Override
    public SliceResponse<PostGetByCategoryResponse> getByCategory(Long userId, Long categoryId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Set<Long> favoritePostIds = postFavoritesRepository.findByUser(user).stream()
                .map(pf -> pf.getPost().getId())
                .collect(Collectors.toSet());

        Category category = categoryQueryService.findById(categoryId);

        Slice<Post> posts = postRepository.findAllByCategoryOrderByStatus(category, pageable);

        Slice<PostGetByCategoryResponse> contents = posts.map(post -> PostGetByCategoryResponse.builder()
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
                .favorites(favoritePostIds.contains(post.getId()))
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
