package com.golfRental.domain.post.service.command;

import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateStatusRequest;
import com.golfRental.domain.post.dto.response.PostCreateResponse;
import com.golfRental.domain.post.dto.response.PostUpdateResponse;
import com.golfRental.domain.post.dto.response.PostUpdateStatusResponse;

public interface PostCommandService {

    /**
     * 게시물 생성
     *
     * @param userId            인증된 유저 ID
     * @param postCreateRequest 게시물 생성시 필요한 데이터
     * @return PostCreateResponse
     */
    PostCreateResponse createPost(Long userId, PostCreateRequest postCreateRequest);

    /**
     * 즐겨찾기 등록
     *
     * @param userId 인증된 유저 ID
     * @param postId 게시물 ID
     */
    void addFavorites(Long userId, Long postId);

    /**
     * 게시물 수정
     *
     * @param userId            인증된 유저 ID
     * @param postId            게시물 ID
     * @param postUpdateRequest 게시물 수정 데이터
     * @return PostUpdateResponse
     */
    PostUpdateResponse updatePost(Long userId, Long postId, PostUpdateRequest postUpdateRequest);

    /**
     * 게시물 거래 상태 수정
     *
     * @param userId                  인증된 유저 ID
     * @param postId                  게시물 ID
     * @param postUpdateStatusRequest 게시물 거래 상태 수정 데이터
     * @return PostUpdateStatusResponse
     */
    PostUpdateStatusResponse updateStatusPost(Long userId, Long postId, PostUpdateStatusRequest postUpdateStatusRequest);

    /**
     * 게시물 삭제
     *
     * @param userId 인증된 유저 ID
     * @param postId 게시물 ID
     */
    void deletePost(Long userId, Long postId);

    /**
     * 즐겨찾기 삭제
     *
     * @param userId 인증된 유저 ID
     * @param postId 게시물 ID
     */
    void deleteFavorites(Long userId, Long postId);
}
