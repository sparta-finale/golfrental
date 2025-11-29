package com.golfRental.domain.post.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.response.*;
import org.springframework.data.domain.Sort;
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
     * @param page      페이지 번호
     * @param size      데이터 개수
     * @param sort      정렬 기준
     * @param direction 정렬 방향
     * @return SliceResponse<PostGetAllResponse>
     */
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllResponse>>> getAll(
            // 카테고리 추가 예정

            int page, int size, String sort, Sort.Direction direction
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
     * @param authUser  토큰 정보
     * @param page      페이지 번호
     * @param size      데이터 개수
     * @param sort      정렬 기준
     * @param direction 정렬 방향
     * @return SliceResponse<PostGetMyResponse>
     */
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetMyResponse>>> getMyPost(
            AuthUser authUser,
            int page, int size, String sort, Sort.Direction direction
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
}
