package com.golfRental.domain.post.service.command;

import com.golfRental.domain.post.dto.request.PostCreateRequest;

public interface PostCommandService {

    /**
     * 게시물 생성 API
     *
     * @param userId            인증된 유저 ID
     * @param postCreateRequest 게시물 생성시 필요한 데이터
     */
    void createPost(Long userId, PostCreateRequest postCreateRequest);
}
