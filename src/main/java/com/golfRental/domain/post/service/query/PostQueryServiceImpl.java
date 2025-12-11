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

            return PostGetAllResponse.from(post, favoritePostIds.contains(post.getId()), postImageResponse);
        });

        return SliceResponse.fromSlice(content);
    }

    @Override
    public PostGetsResponse getPost(Long userId, Long postId) {
        User user = userQueryService.findById(userId);

        Post post = findById(postId);

        boolean postFavorites = postFavoritesRepository.existsByUserAndPost(user, post);

        List<PostImageResponse> postImages = getImageResponseList(post);

        return PostGetsResponse.from(post, postFavorites, postImages);
    }

    @Override
    public SliceResponse<PostGetMyResponse> getMyPost(Long userId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Set<Long> favoritePostIds = postFavoritesRepository.findPostIdsByUser(user);

        Slice<Post> posts = postRepository.findAllByUserOrderByStatus(user, pageable);

        Slice<PostGetMyResponse> contents = posts.map(post -> {
            PostImageResponse postImageResponse = getThumbnailResponse(post);

            return PostGetMyResponse.from(post, favoritePostIds.contains(post.getId()), postImageResponse);
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

            return PostGetByCategoryResponse.from(post, favoritePostIds.contains(post.getId()), postImageResponse);
        });

        return SliceResponse.fromSlice(contents);
    }

    @Override
    public SliceResponse<PostGetByFavoritesResponse> getByFavorites(Long userId, Pageable pageable) {
        User user = userQueryService.findById(userId);

        Slice<PostFavorites> postFavorites = postFavoritesRepository.findByUserWithPostAndUser(user, pageable);

        Slice<PostGetByFavoritesResponse> contents = postFavorites.map(pf -> {
            PostImageResponse postImageResponse = getThumbnailResponse(pf.getPost());

            return PostGetByFavoritesResponse.from(pf, true, postImageResponse);
        });

        return SliceResponse.fromSlice(contents);
    }

    @Override
    public SliceResponse<PostGetAllPublicResponse> getAllPublic(Pageable pageable) {
        Slice<Post> posts = postRepository.findAllOrderByStatus(pageable);

        Slice<PostGetAllPublicResponse> content = posts.map(post -> {
            PostImageResponse postImageResponse = getThumbnailResponse(post);

            return PostGetAllPublicResponse.from(post, false, postImageResponse);
        });

        return SliceResponse.fromSlice(content);
    }

    @Override
    public PostGetsPublicResponse getPostPublic(Long postId) {
        Post post = findById(postId);

        List<PostImageResponse> postImages = getImageResponseList(post);

        return PostGetsPublicResponse.from(post, false, postImages);
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

    @Override
    public List<Post> findAll() {
        return postRepository.findAllOrderByTradeStatus();
    }

    @Override
    public List<Post> findAllByCategoryName(String categoryName) {
        return postRepository.findAllByCategoryName(categoryName);
    }

    private PostImageResponse toPostImageResponse(PostImage image) {
        if (image == null) return null;
        return PostImageResponse.create(image.getImage().getUrl(), image.getIsThumbnail(), image.getSortOrder());
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
