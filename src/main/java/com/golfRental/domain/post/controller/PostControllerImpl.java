package com.golfRental.domain.post.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateStatusRequest;
import com.golfRental.domain.post.dto.response.*;
import com.golfRental.domain.post.message.PostSuccessMessage;
import com.golfRental.domain.post.service.command.PostCommandService;
import com.golfRental.domain.post.service.query.PostQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostControllerImpl implements PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @Override
    @PostMapping("/posts")
    public ResponseEntity<CommonApiResponse<PostCreateResponse>> createPost(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody PostCreateRequest postCreateRequest
    ) {
        PostCreateResponse postCreateResponse = postCommandService.createPost(authUser.getUserId(), postCreateRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postCreateResponse.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(CommonApiResponse.created(postCreateResponse, PostSuccessMessage.POST_CREATED).getBody());
    }

    @Override
    @PostMapping("/posts/{postId}/favorites")
    public ResponseEntity<CommonApiResponse<Void>> addFavorites(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId
    ) {
        postCommandService.addFavorites(authUser.getUserId(), postId);

        return CommonApiResponse.success(null, PostSuccessMessage.POST_ADD_FAVORITES);
    }

    @Override
    @GetMapping("/posts")
    public ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllResponse>>> getAll(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SliceResponse<PostGetAllResponse> posts = postQueryService.getAll(authUser.getUserId(), pageable);

        return CommonApiResponse.sliceSuccess(posts, PostSuccessMessage.POST_GET_ALL);
    }

    @Override
    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommonApiResponse<PostGetsResponse>> getPost(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId
    ) {
        PostGetsResponse postGetsResponse = postQueryService.getPost(authUser.getUserId(), postId);

        return CommonApiResponse.success(postGetsResponse, PostSuccessMessage.POST_GETS);
    }

    @Override
    @GetMapping("/posts/my")
    public ResponseEntity<CommonApiResponse<SliceResponse<PostGetMyResponse>>> getMyPost(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SliceResponse<PostGetMyResponse> posts = postQueryService.getMyPost(authUser.getUserId(), pageable);

        return CommonApiResponse.sliceSuccess(posts, PostSuccessMessage.POST_GET_MY);
    }

    @Override
    @GetMapping("/posts/categories/{categoryId}")
    public ResponseEntity<CommonApiResponse<SliceResponse<PostGetByCategoryResponse>>> getByCategory(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long categoryId,
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SliceResponse<PostGetByCategoryResponse> posts = postQueryService.getByCategory(authUser.getUserId(), categoryId, pageable);

        return CommonApiResponse.sliceSuccess(posts, PostSuccessMessage.POST_GET_BY_CATEGORY);
    }

    @Override
    @GetMapping("/posts/favorites")
    public ResponseEntity<CommonApiResponse<SliceResponse<PostGetByFavoritesResponse>>> getByFavorites(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(page = 0, size = 20, sort = "post.createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SliceResponse<PostGetByFavoritesResponse> posts = postQueryService.getByFavorites(authUser.getUserId(), pageable);

        return CommonApiResponse.sliceSuccess(posts, PostSuccessMessage.POST_GET_BY_FAVORITES);
    }

    @Override
    @GetMapping("/public/posts")
    public ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllPublicResponse>>> getAllPublic(
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SliceResponse<PostGetAllPublicResponse> posts = postQueryService.getAllPublic(pageable);

        return CommonApiResponse.sliceSuccess(posts, PostSuccessMessage.POST_GET_ALL_PUBLIC);
    }

    @Override
    @PutMapping("/posts/{postId}")
    public ResponseEntity<CommonApiResponse<PostUpdateResponse>> updatePost(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest postUpdateRequest
    ) {
        PostUpdateResponse postUpdateResponse = postCommandService.updatePost(authUser.getUserId(), postId, postUpdateRequest);

        return CommonApiResponse.success(postUpdateResponse, PostSuccessMessage.POST_UPDATED);
    }

    @Override
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<CommonApiResponse<PostUpdateStatusResponse>> updateStatusPost(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateStatusRequest postUpdateStatusRequest
    ) {
        PostUpdateStatusResponse postUpdateStatusResponse = postCommandService.updateStatusPost(authUser.getUserId(), postId, postUpdateStatusRequest);

        return CommonApiResponse.success(postUpdateStatusResponse, PostSuccessMessage.POST_UPDATED_STATUS);
    }

    @Override
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<CommonApiResponse<Void>> deletePost(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId
    ) {
        postCommandService.deletePost(authUser.getUserId(), postId);

        return CommonApiResponse.deleteSuccess(PostSuccessMessage.POST_DELETED);
    }

    @Override
    @DeleteMapping("/posts/{postId}/favorites")
    public ResponseEntity<CommonApiResponse<Void>> deleteFavorites(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId
    ) {
        postCommandService.deleteFavorites(authUser.getUserId(), postId);

        return CommonApiResponse.deleteSuccess(PostSuccessMessage.POST_FAVORITES_DELETED);
    }
}
