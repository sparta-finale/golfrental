package com.golfRental.domain.post.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.post.dto.response.*;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.reservation.dto.response.ReservationGetAllResponse;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {

    /**
     * 게시물 목록 조회
     *
     * @param pageable 페이지 데이터
     * @return SliceResponse<PostGetAllResponse>
     */
    SliceResponse<PostGetAllResponse> getAll(Long userId, Pageable pageable);

    /**
     * 게시물 상세 조회
     *
     * @param postId 게시물 ID
     * @return PostGetsResponse
     */
    PostGetsResponse getPost(Long userId, Long postId);

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
    SliceResponse<PostGetByCategoryResponse> getByCategory(Long userId, Long categoryId, Pageable pageable);

    /**
     * 즐겨찾기를 통한 게시물 조회
     *
     * @param userId   유저 ID
     * @param pageable 페이지 데이터
     * @return SliceResponse<PostGetByFavoritesResponse>
     */
    SliceResponse<PostGetByFavoritesResponse> getByFavorites(Long userId, Pageable pageable);

    /**
     * 게시물 목록 조회(public)
     *
     * @param pageable 페이지 데이터
     * @return SliceResponse<PostGetAllPublicResponse>
     */
    SliceResponse<PostGetAllPublicResponse> getAllPublic(Pageable pageable);

    /**
     * 게시물 상세 조회(public)
     *
     * @param postId 게시물 ID
     * @return PostGetsPublicResponse
     */
    PostGetsPublicResponse getPostPublic(Long postId);

    /**
     * 게시물 예약된 날짜 조회
     *
     * @param postId   게시물 ID
     * @param pageable 페이지 데이터
     * @return SliceResponse<ReservationGetAllResponse>
     */
    SliceResponse<ReservationGetAllResponse> getPostReservation(Long postId, Pageable pageable);

    Post findById(Long postId);
}
