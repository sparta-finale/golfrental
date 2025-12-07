package com.golfRental.domain.post.service.command;

import com.golfRental.domain.category.entity.Category;
import com.golfRental.domain.category.service.query.CategoryQueryService;
import com.golfRental.domain.image.entity.Image;
import com.golfRental.domain.image.service.query.ImageQueryService;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateStatusRequest;
import com.golfRental.domain.post.dto.response.PostCreateResponse;
import com.golfRental.domain.post.dto.response.PostUpdateResponse;
import com.golfRental.domain.post.dto.response.PostUpdateStatusResponse;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.entity.PostFavorites;
import com.golfRental.domain.post.entity.PostImage;
import com.golfRental.domain.post.exception.PostErrorCode;
import com.golfRental.domain.post.exception.PostException;
import com.golfRental.domain.post.repository.PostFavoritesRepository;
import com.golfRental.domain.post.repository.PostImageRepository;
import com.golfRental.domain.post.repository.PostRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final PostFavoritesRepository postFavoritesRepository;
    private final UserQueryService userQueryService;
    private final CategoryQueryService categoryQueryService;
    private final ImageQueryService imageQueryService;
    private final PostImageRepository postImageRepository;

    @Override
    public PostCreateResponse createPost(Long userId, PostCreateRequest postCreateRequest) {
        User user = userQueryService.findById(userId);
        Category category = categoryQueryService.findById(postCreateRequest.getCategoryId());

        Post post = Post.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .methodOfReceive(postCreateRequest.getMethodOfReceive())
                .methodOfReturn(postCreateRequest.getMethodOfReturn())
                .price(postCreateRequest.getPrice())
                .deposit(postCreateRequest.getDeposit())
                .dailyRate(postCreateRequest.getDailyRate())
                .user(user)
                .category(category)
                .build();

        Post savedPost = postRepository.save(post);

        List<Long> imageIds = postCreateRequest.getImages().stream()
                .map(PostCreateRequest.PostImageInfoCreateRequest::imageId)
                .toList();

        // 중복 imageId 검증
        long distinctCount = imageIds.stream().distinct().count();
        if (distinctCount != imageIds.size()) {
            throw new PostException(PostErrorCode.DUPLICATE_IMAGE_IDS);
        }

        List<Image> images = imageQueryService.findAllByImage(imageIds);

        List<PostImage> postImages = postCreateRequest.getImages().stream()
                .map(imageInfo -> {
                    Image image = images.stream()
                            .filter(img -> Objects.equals(img.getId(), imageInfo.imageId()))
                            .findFirst()
                            .orElse(null);

                    return PostImage.builder()
                            .post(post)
                            .image(image)
                            .isThumbnail(imageInfo.isThumbnail())
                            .sortOrder(imageInfo.sortOrder())
                            .build();
                }).toList();

        postImageRepository.saveAll(postImages);

        List<PostCreateResponse.PostImageInfoCreateResponse> imagesResponse = postImages.stream()
                .map(postImage -> PostCreateResponse.PostImageInfoCreateResponse.builder()
                        .url(postImage.getImage().getUrl())
                        .isThumbnail(postImage.getIsThumbnail())
                        .sortOrder(postImage.getSortOrder())
                        .build()
                ).toList();

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
                .categoryId(category.getId())
                .categoryName(category.getName())
                .favorites(false)
                .images(imagesResponse)
                .build();
    }

    @Override
    public void addFavorites(Long userId, Long postId) {
        User user = userQueryService.findById(userId);

        Post post = postRepository.findByIdWithUserAndCategory(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_INVALID_ID)
        );

        if (postFavoritesRepository.existsByUserAndPost(user, post)) {
            throw new PostException(PostErrorCode.POST_DUPLICATION_FAVORITES); // Or a more specific error
        }

        PostFavorites postFavorites = PostFavorites.builder()
                .user(user)
                .post(post)
                .build();

        postFavoritesRepository.save(postFavorites);
    }

    @Override
    public PostUpdateResponse updatePost(Long userId, Long postId, PostUpdateRequest postUpdateRequest) {
        User user = userQueryService.findById(userId);

        Post post = findPostAndCheckOwner(userId, postId);

        Category category = categoryQueryService.findById(postUpdateRequest.getCategoryId());

        post.update(postUpdateRequest, category);

        boolean postFavorites = postFavoritesRepository.existsByUserAndPost(user, post);

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
                .categoryId(post.getCategory().getId())
                .categoryName(post.getCategory().getName())
                .favorites(postFavorites)
                .build();
    }

    @Override
    public PostUpdateStatusResponse updateStatusPost(Long userId, Long postId, PostUpdateStatusRequest postUpdateStatusRequest) {
        Post post = findPostAndCheckOwner(userId, postId);

        post.updateStatus(postUpdateStatusRequest.getTradeStatus());

        return PostUpdateStatusResponse.builder()
                .tradeStatus(post.getTradeStatus())
                .build();
    }

    @Override
    public void deletePost(Long userId, Long postId) {
        Post post = findPostAndCheckOwner(userId, postId);

        post.delete();
    }

    @Override
    public void deleteFavorites(Long userId, Long postId) {
        User user = userQueryService.findById(userId);
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_INVALID_ID)
        );
        PostFavorites postFavorites = postFavoritesRepository.findByUserAndPost(user, post).orElseThrow(
                () -> new PostException(PostErrorCode.POST_NOT_FAVORITES)
        );

        postFavoritesRepository.delete(postFavorites);
    }

    private Post findPostAndCheckOwner(Long userId, Long postId) {
        Post post = postRepository.findByIdWithUserAndCategory(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_INVALID_ID)
        );

        if (!Objects.equals(post.getUser().getId(), userId)) {
            throw new PostException(PostErrorCode.POST_NOT_EQUAL_CREATOR);
        }

        return post;
    }
}
