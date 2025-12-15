package com.golfRental.domain.post.service.command;

import com.golfRental.domain.category.entity.Category;
import com.golfRental.domain.category.service.query.CategoryQueryService;
import com.golfRental.domain.image.entity.Image;
import com.golfRental.domain.image.service.query.ImageQueryService;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostImageDeleteRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateStatusRequest;
import com.golfRental.domain.post.dto.response.*;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

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
        Category category = categoryQueryService.findById(postCreateRequest.categoryId());

        Post post = Post.create(
                postCreateRequest.title(), postCreateRequest.content(),
                postCreateRequest.methodOfReceive(), postCreateRequest.methodOfReturn(),
                postCreateRequest.price(), postCreateRequest.deposit(),
                postCreateRequest.dailyRate(), user, category
        );

        Post savedPost = postRepository.save(post);

        List<Long> imageIds = postCreateRequest.images().stream()
                .map(PostCreateRequest.PostImageInfoCreateRequest::imageId)
                .toList();

        long distinctCount = imageIds.stream().distinct().count();
        if (distinctCount != imageIds.size()) {
            throw new PostException(PostErrorCode.DUPLICATE_IMAGE_IDS);
        }

        List<Image> images = imageQueryService.findAllByImage(imageIds);

        Map<Long, Image> imageMap = images.stream()
                .collect(toMap(Image::getId, identity()));

        List<PostImage> postImages = postCreateRequest.images().stream()
                .map(imageInfo -> PostImage.create(
                                imageInfo.isThumbnail(), imageInfo.sortOrder(), post, imageMap.get(imageInfo.imageId())
                        )
                ).toList();

        postImageRepository.saveAll(postImages);

        List<PostImageResponse> imagesResponse = postImages.stream()
                .map(postImage -> PostImageResponse.from(
                                postImage.getImage().getUrl(), postImage.getIsThumbnail(), postImage.getSortOrder()
                        )
                ).toList();

        return PostCreateResponse.from(savedPost, user, category, imagesResponse);
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

        PostFavorites postFavorites = PostFavorites.create(user, post);

        postFavoritesRepository.save(postFavorites);
    }

    @Override
    @CacheEvict(value = "postListCache", allEntries = true)
    public PostUpdateResponse updatePost(Long userId, Long postId, PostUpdateRequest postUpdateRequest) {
        User user = userQueryService.findById(userId);

        Post post = findPostAndCheckOwner(userId, postId);

        Category category = categoryQueryService.findById(postUpdateRequest.categoryId());

        post.update(postUpdateRequest, category);

        boolean postFavorites = postFavoritesRepository.existsByUserAndPost(user, post);

        List<PostImageResponse> postImages = createPostImageResponses(post);

        return PostUpdateResponse.from(post, postFavorites, postImages);
    }

    @Override
    public PostImageThumbnailUpdateResponse updateThumbnail(Long userId, Long postId, Long imageId) {
        User user = userQueryService.findById(userId);
        Post post = findPostAndCheckOwner(userId, postId);
        Image image = imageQueryService.findById(imageId);

        boolean postFavorites = postFavoritesRepository.existsByUserAndPost(user, post);

        if (!postImageRepository.existsByPostAndImageAndDeletedAtIsNull(post, image)) {
            throw new PostException(PostErrorCode.POST_IMAGE_NOT_EXIST);
        }

        post.getPostImages().forEach(postImage -> {
            boolean isNewThumbnail = Objects.equals(postImage.getImage().getId(), imageId);
            postImage.updateThumbnail(isNewThumbnail);
        });

        List<PostImageResponse> postImages = createPostImageResponses(post);

        return PostImageThumbnailUpdateResponse.from(post, postFavorites, postImages);
    }

    @Override
    public PostUpdateStatusResponse updateStatusPost(Long userId, Long postId, PostUpdateStatusRequest postUpdateStatusRequest) {
        Post post = findPostAndCheckOwner(userId, postId);

        post.updateStatus(postUpdateStatusRequest.tradeStatus());

        return PostUpdateStatusResponse.from(post);
    }

    @Override
    @CacheEvict(value = "postListCache", allEntries = true)
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

    @Override
    public void deleteImages(Long userId, Long postId, PostImageDeleteRequest postImageDeleteRequest) {
        Post post = postRepository.findByIdWithUserAndCategory(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_INVALID_ID)
        );

        if (!Objects.equals(post.getUser().getId(), userId)) {
            throw new PostException(PostErrorCode.POST_NOT_EQUAL_CREATOR);
        }

        List<PostImage> activeImages = post.getPostImages().stream()
                .filter(postImage -> postImage.getDeletedAt() == null)
                .toList();

        List<PostImage> imagesToDelete = activeImages.stream()
                .filter(postImage -> postImageDeleteRequest.imageIds().contains(postImage.getImage().getId()))
                .toList();

        if (imagesToDelete.size() != postImageDeleteRequest.imageIds().size()) {
            throw new PostException(PostErrorCode.POST_IMAGE_NOT_EXIST);
        }
        if (activeImages.size() - imagesToDelete.size() < 1) {
            throw new PostException(PostErrorCode.POST_IMAGE_NEED);
        }

        boolean thumbnailWillBeDeleted = imagesToDelete.stream()
                .anyMatch(PostImage::getIsThumbnail);

        imagesToDelete.forEach(postImage -> {
            postImage.delete();
            postImage.getImage().delete();
        });

        if (thumbnailWillBeDeleted) {
            post.getPostImages().stream()
                    .filter(postImage -> postImage.getDeletedAt() == null)
                    .min(java.util.Comparator.comparingInt(PostImage::getSortOrder))
                    .ifPresent(postImage -> postImage.updateThumbnail(true));
        }
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

    private List<PostImageResponse> createPostImageResponses(Post post) {
        return post.getPostImages().stream()
                .map(postImage -> PostImageResponse.from(
                                postImage.getImage().getUrl(), postImage.getIsThumbnail(), postImage.getSortOrder()
                        )
                ).toList();
    }
}
