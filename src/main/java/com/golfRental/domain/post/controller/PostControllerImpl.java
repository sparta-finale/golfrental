package com.golfRental.domain.post.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.response.PostCreateResponse;
import com.golfRental.domain.post.dto.response.PostGetAllResponse;
import com.golfRental.domain.post.dto.response.PostGetMyResponse;
import com.golfRental.domain.post.dto.response.PostGetsResponse;
import com.golfRental.domain.post.message.PostSuccessMessage;
import com.golfRental.domain.post.service.command.PostCommandService;
import com.golfRental.domain.post.service.query.PostQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @GetMapping("/posts")
    public ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllResponse>>> getAll(
            // 카테고리 추가 예정

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        SliceResponse<PostGetAllResponse> posts = postQueryService.getAll(pageable);

        return CommonApiResponse.sliceSuccess(posts, PostSuccessMessage.POST_GET_ALL);
    }

    @Override
    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommonApiResponse<PostGetsResponse>> getPost(
            @PathVariable Long postId
    ) {
        PostGetsResponse postGetsResponse = postQueryService.getPost(postId);

        return CommonApiResponse.success(postGetsResponse, PostSuccessMessage.POST_GETS);
    }

    @Override
    @GetMapping("/posts/my")
    public ResponseEntity<CommonApiResponse<SliceResponse<PostGetMyResponse>>> getMyPost(
            @AuthenticationPrincipal AuthUser authUser,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        SliceResponse<PostGetMyResponse> posts = postQueryService.getMyPost(authUser.getUserId(), pageable);

        return CommonApiResponse.sliceSuccess(posts, PostSuccessMessage.POST_GET_MY);
    }
}
