package com.golfRental.domain.post.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.post.dto.response.PostGetAllResponse;
import com.golfRental.domain.post.dto.response.PostGetByCategoryResponse;
import com.golfRental.domain.post.dto.response.PostGetMyResponse;
import com.golfRental.domain.post.dto.response.PostGetsResponse;
import com.golfRental.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {

    /**
     * 게시물 목록 조회
     *
     * @param pageable 페이지 데이터
     * @return SliceResponse<PostGetAllResponse>
     */
    SliceResponse<PostGetAllResponse> getAll(Pageable pageable);

    /**
     * 게시물 상세 조회
     *
     * @param postId 게시물 ID
     * @return PostGetsResponse
     */
    PostGetsResponse getPost(Long postId);

    /**
     * 나의 게시물 조회
     *
     * @param userId   유저 ID
     * @param pageable 페이지 데이터
     * @return SliceResponse<PostGetMyResponse>
     */
    SliceResponse<PostGetMyResponse> getMyPost(Long userId, Pageable pageable);

    /**
     * 카테고리를 통한 게시물 조회
     *
     * @param categoryId 카테고리 ID
     * @param pageable   페이지 데이터
     * @return SliceResponse<PostGetByCategoryResponse>
     */
    SliceResponse<PostGetByCategoryResponse> getByCategory(Long categoryId, Pageable pageable);

    Post findById(Long postId);
}
