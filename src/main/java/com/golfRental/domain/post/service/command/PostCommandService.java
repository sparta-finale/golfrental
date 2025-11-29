package com.golfRental.domain.post.service.command;

import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.response.PostCreateResponse;
import com.golfRental.domain.post.dto.response.PostUpdateResponse;

public interface PostCommandService {

    /**
     * 게시물 생성 API
     *
     * @param userId            인증된 유저 ID
     * @param postCreateRequest 게시물 생성시 필요한 데이터
     * @return PostCreateResponse
     */
    PostCreateResponse createPost(Long userId, PostCreateRequest postCreateRequest);

    /**
     * 게시물 수정 API
     *
     * @param userId            인증된 유저 ID
     * @param postId            게시물 ID
     * @param postUpdateRequest 게시물 수정 데이터
     * @return PostUpdateResponse
     */
    PostUpdateResponse updatePost(Long userId, Long postId, PostUpdateRequest postUpdateRequest);
}
