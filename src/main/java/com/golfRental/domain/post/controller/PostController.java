package com.golfRental.domain.post.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateStatusRequest;
import com.golfRental.domain.post.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PostController {

    /**
     * 게시물 생성 API
     *
     * @param authUser          토큰 정보
     * @param postCreateRequest 게시물 생성을 위한 데이터
     * @return PostCreateResponse
     */
    ResponseEntity<CommonApiResponse<PostCreateResponse>> createPost(
            AuthUser authUser, PostCreateRequest postCreateRequest
    );

    /**
     * 게시물 목록 조회 API
     *
     * @param pageable 페이지 정보
     * @return SliceResponse<PostGetAllResponse>
     */
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllResponse>>> getAll(
            // 카테고리 추가 예정
            Pageable pageable
    );

    /**
     * 게시물 상세 조회 API
     *
     * @param postId 게시물 아이디
     * @return PostGetsResponse
     */
    ResponseEntity<CommonApiResponse<PostGetsResponse>> getPost(
            Long postId
    );

    /**
     * 나의 게시물 조회 API
     *
     * @param authUser 토큰 정보
     * @param pageable 페이지 정보
     * @return SliceResponse<PostGetMyResponse>
     */
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetMyResponse>>> getMyPost(
            AuthUser authUser,
            Pageable pageable
    );

    /**
     * 카테고리를 통한 게시물 조회 API
     *
     * @param categoryId 카테고리 ID
     * @param pageable   페이지 정보
     * @return SliceResponse<PostGetByCategoryResponse>
     */
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetByCategoryResponse>>> getByCategory(
            Long categoryId, Pageable pageable
    );

    /**
     * 게시물 수정 API
     *
     * @param authUser 토큰 정보
     * @param postId   게시물 ID
     * @return PostUpdateResponse
     */
    ResponseEntity<CommonApiResponse<PostUpdateResponse>> updatePost(
            AuthUser authUser,
            Long postId, PostUpdateRequest postUpdateRequest
    );

    /**
     * 게시글 거래 상태 수정 API
     *
     * @param authUser 토큰 정보
     * @param postId   게시물 ID
     * @return PostUpdateStatusResponse
     */
    ResponseEntity<CommonApiResponse<PostUpdateStatusResponse>> updateStatusPost(
            AuthUser authUser,
            Long postId, PostUpdateStatusRequest postUpdateStatusRequest
    );

    /**
     * 게시물 삭제 API
     *
     * @param authUser 토큰 정보
     * @param postId   게시물 ID
     * @return void
     */
    ResponseEntity<CommonApiResponse<Void>> deletePost(
            AuthUser authUser,
            Long postId
    );
}
