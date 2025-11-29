package com.golfRental.domain.post.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.response.PostCreateResponse;
import com.golfRental.domain.post.dto.response.PostGetAllResponse;
import com.golfRental.domain.post.dto.response.PostGetsResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface PostController {

    /**
     * 게시물 생성 API
     *
     * @param authUser          토큰 정보
     * @param postCreateRequest 게시물 생성을 위한 데이터
     * @return PostCreateResponse
     */
    ResponseEntity<CommonApiResponse<PostCreateResponse>> createPost(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody PostCreateRequest postCreateRequest
    );

    /**
     * 게시물 목록 조회 API
     *
     * @param page      페이지 번호
     * @param size      데이터 개수
     * @param sort      정렬 기준
     * @param direction 정렬 방향
     * @return SliceResponse<PostGetAllResponse>
     */
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllResponse>>> getAll(
            // 카테고리 추가 예정

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    );

    /**
     * 게시물 상세 조회 API
     *
     * @param postId 게시물 아이디
     * @return PostGetsResponse
     */
    ResponseEntity<CommonApiResponse<PostGetsResponse>> getPost(
            @PathVariable Long postId
    );

    ;
}
