package com.golfRental.domain.post.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.category.entity.Category;
import com.golfRental.domain.category.service.query.CategoryQueryService;
import com.golfRental.domain.post.dto.response.*;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.entity.PostFavorites;
import com.golfRental.domain.post.entity.PostImage;
import com.golfRental.domain.post.exception.PostErrorCode;
import com.golfRental.domain.post.exception.PostException;
import com.golfRental.domain.post.repository.PostFavoritesRepository;
import com.golfRental.domain.post.repository.PostRepository;
import com.golfRental.domain.reservation.dto.response.ReservationGetAllResponse;
import com.golfRental.domain.reservation.service.query.ReservationQueryService;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final PostFavoritesRepository postFavoritesRepository;
    private final UserQueryService userQueryService;
    private final CategoryQueryService categoryQueryService;
    private final ReservationQueryService reservationQueryService;

    @Override
    public SliceResponse<PostGetAllResponse> getAll(Long userId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Set<Long> favoritePostIds = postFavoritesRepository.findPostIdsByUser(user);

        Slice<Post> posts = postRepository.findAllOrderByStatus(pageable);

        Slice<PostGetAllResponse> content = posts.map(post -> {
            PostImageResponse postImageResponse = getThumbnailResponse(post);

            return PostGetAllResponse.builder()
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
                    .image(postImageResponse)
                    .build();
        });

        return SliceResponse.fromSlice(content);
    }

    @Override
    public PostGetsResponse getPost(Long userId, Long postId) {
        User user = userQueryService.findById(userId);

        Post post = findById(postId);

        boolean postFavorites = postFavoritesRepository.existsByUserAndPost(user, post);

        List<PostImageResponse> postImages = getImageResponseList(post);

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
                .images(postImages)
                .build();
    }

    @Override
    public SliceResponse<PostGetMyResponse> getMyPost(Long userId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Set<Long> favoritePostIds = postFavoritesRepository.findPostIdsByUser(user);

        Slice<Post> posts = postRepository.findAllByUserOrderByStatus(user, pageable);

        Slice<PostGetMyResponse> contents = posts.map(post -> {
            PostImageResponse postImageResponse = getThumbnailResponse(post);

            return PostGetMyResponse.builder()
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
                    .image(postImageResponse)
                    .build();
        });

        return SliceResponse.fromSlice(contents);
    }

    @Override
    public SliceResponse<PostGetByCategoryResponse> getByCategory(Long userId, Long categoryId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Set<Long> favoritePostIds = postFavoritesRepository.findPostIdsByUser(user);

        Category category = categoryQueryService.findById(categoryId);

        Slice<Post> posts = postRepository.findAllByCategoryOrderByStatus(category, pageable);

        Slice<PostGetByCategoryResponse> contents = posts.map(post -> {
            PostImageResponse postImageResponse = getThumbnailResponse(post);

            return PostGetByCategoryResponse.builder()
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
                    .image(postImageResponse)
                    .build();
        });

        return SliceResponse.fromSlice(contents);
    }

    @Override
    public SliceResponse<PostGetByFavoritesResponse> getByFavorites(Long userId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Slice<PostFavorites> postFavorites = postFavoritesRepository.findByUserWithPostAndUser(user, pageable);

        Slice<PostGetByFavoritesResponse> contents = postFavorites.map(pf -> {
            PostImageResponse postImageResponse = getThumbnailResponse(pf.getPost());

            return PostGetByFavoritesResponse.builder()
                    .id(pf.getPost().getId())
                    .title(pf.getPost().getTitle())
                    .content(pf.getPost().getContent())
                    .methodOfReceive(pf.getPost().getMethodOfReceive())
                    .methodOfReturn(pf.getPost().getMethodOfReturn())
                    .price(pf.getPost().getPrice())
                    .deposit(pf.getPost().getDeposit())
                    .dailyRate(pf.getPost().getDailyRate())
                    .tradeStatus(pf.getPost().getTradeStatus())
                    .userId(pf.getPost().getUser().getId())
                    .username(pf.getPost().getUser().getUsername())
                    .address(pf.getPost().getUser().getAddress())
                    .nickname(pf.getPost().getUser().getNickname())
                    .categoryId(pf.getPost().getCategory().getId())
                    .categoryName(pf.getPost().getCategory().getName())
                    .favorites(true)
                    .image(postImageResponse)
                    .build();
        });

        return SliceResponse.fromSlice(contents);
    }

    @Override
    public SliceResponse<PostGetAllPublicResponse> getAllPublic(Pageable pageable) {
        Slice<Post> posts = postRepository.findAllOrderByStatus(pageable);

        Slice<PostGetAllPublicResponse> content = posts.map(post -> {
            PostImageResponse postImageResponse = getThumbnailResponse(post);

            return PostGetAllPublicResponse.builder()
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
                    .favorites(false)
                    .image(postImageResponse)
                    .build();
        });

        return SliceResponse.fromSlice(content);
    }

    @Override
    public PostGetsPublicResponse getPostPublic(Long postId) {
        Post post = findById(postId);

        List<PostImageResponse> postImages = getImageResponseList(post);

        return PostGetsPublicResponse.builder()
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
                .favorites(false)
                .images(postImages)
                .build();
    }

    @Override
    public SliceResponse<ReservationGetAllResponse> getPostReservation(Long postId, Pageable pageable) {
        return reservationQueryService.findByPostId(
                postId, pageable);
    }

    @Override
    public Post findById(Long postId) {
        return postRepository.findByIdWithUserAndCategory(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_INVALID_ID)
        );
    }

    private PostImageResponse toPostImageResponse(PostImage image) {
        if (image == null) return null;
        return PostImageResponse.builder()
                .url(image.getImage().getUrl())
                .isThumbnail(image.getIsThumbnail())
                .sortOrder(image.getSortOrder())
                .build();
    }

    private PostImageResponse getThumbnailResponse(Post post) {
        return post.getPostImages().stream()
                .filter(PostImage::getIsThumbnail)
                .findFirst()
                .map(this::toPostImageResponse)
                .orElse(null);
    }

    private List<PostImageResponse> getImageResponseList(Post post) {
        return post.getPostImages().stream()
                .map(this::toPostImageResponse)
                .sorted(Comparator.comparing(PostImageResponse::sortOrder))
                .toList();
    }
}
