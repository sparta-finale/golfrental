package com.golfRental.domain.post.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.response.PostCreateResponse;
import com.golfRental.domain.post.dto.response.PostGetAllResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface PostController {

    ResponseEntity<CommonApiResponse<PostCreateResponse>> createPost(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody PostCreateRequest postCreateRequest
    );

    ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllResponse>>> getAll(
            // 카테고리 추가 예정

            @RequestParam(defaultValue = "0") int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    );
}
