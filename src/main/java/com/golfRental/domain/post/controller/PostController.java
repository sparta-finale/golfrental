package com.golfRental.domain.post.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.post.dto.request.PostCreateRequest;
import com.golfRental.domain.post.dto.request.PostImageDeleteRequest;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
import com.golfRental.domain.post.dto.request.PostUpdateStatusRequest;
import com.golfRental.domain.post.dto.response.*;
import com.golfRental.domain.reservation.dto.response.ReservationGetAllResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "게시물 관리", description = "게시물 관련 API")
public interface PostController {

    @Operation(
            summary = "게시물 생성",
            description = "게시물을 생성합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시물 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "중복된 이미지 ID 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PostCreateResponse>> createPost(
            AuthUser authUser,
            PostCreateRequest postCreateRequest
    );

    @Operation(
            summary = "게시물 즐겨찾기 추가",
            description = "게시물을 자신의 즐겨찾기에 추가합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "즐겨찾기 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 해당 게시물의 즐겨찾기 추가가 되어있음 실패"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> addFavorites(
            AuthUser authUser,
            Long postId
    );

    @Operation(
            summary = "게시물 목록 조회",
            description = "게시물 목록을 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 목록 조회 성공")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllResponse>>> getAll(
            AuthUser authUser,
            Pageable pageable
    );

    @Operation(
            summary = "게시물 상세 조회",
            description = "게시물을 상세 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 상세 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PostGetsResponse>> getPost(
            AuthUser authUser,
            Long postId
    );

    @Operation(
            summary = "나의 게시물 목록 조회",
            description = "나의 게시물 목록을 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "나의 게시물 목록 조회 성공")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetMyResponse>>> getMyPost(
            AuthUser authUser,
            Pageable pageable
    );

    @Operation(
            summary = "카테고리를 통한 게시물 목록 조회",
            description = "카테고리를 통한 게시물 목록을 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리를 통한 게시물 목록 조회 성공")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetByCategoryResponse>>> getByCategory(
            AuthUser authUser,
            Long categoryId, Pageable pageable
    );

    @Operation(
            summary = "즐겨찾기를 통한 게시물 목록 조회",
            description = "즐겨찾기를 통한 게시물 목록을 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "즐겨찾기를 통한 게시물 목록 조회 성공")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetByFavoritesResponse>>> getByFavorites(
            AuthUser authUser,
            Pageable pageable
    );

    @Operation(
            summary = "게시물 목록 조회(public)",
            description = "게시물 목록을 조회합니다.(public)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 목록 조회(public) 성공")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<PostGetAllPublicResponse>>> getAllPublic(
            Pageable pageable
    );

    @Operation(
            summary = "게시물 상세 조회(public)",
            description = "게시물을 상세 조회합니다.(public)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 상세 조회(public) 성공"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PostGetsPublicResponse>> getPostPublic(
            Long postId
    );

    @Operation(
            summary = "게시물 예약된 날짜 조회",
            description = "게시물에 예약된 날짜를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 예약된 날짜 조회 성공")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<ReservationGetAllResponse>>> getPostReservation(
            Long postId, Pageable pageable
    );

    @Operation(
            summary = "게시물 수정",
            description = "게시물을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 수정 성공"),
                    @ApiResponse(responseCode = "403", description = "게시물의 생성자만이 수정을 할 수 있음 실패"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PostUpdateResponse>> updatePost(
            AuthUser authUser,
            Long postId, PostUpdateRequest postUpdateRequest
    );

    @Operation(
            summary = "게시물 거래 상태 수정",
            description = "게시물 거래 상태를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 거래 상태 수정 성공"),
                    @ApiResponse(responseCode = "403", description = "게시물의 생성자만이 수정을 할 수 있음 실패"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PostUpdateStatusResponse>> updateStatusPost(
            AuthUser authUser,
            Long postId, PostUpdateStatusRequest postUpdateStatusRequest
    );

    @Operation(
            summary = "게시물 대표 이미지 수정",
            description = "게시물 대표 이미지를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 대표 이미지 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "게시물에 이미지가 존재하지 않음 실패"),
                    @ApiResponse(responseCode = "403", description = "게시물의 생성자만이 수정을 할 수 있음 실패"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PostImageThumbnailUpdateResponse>> updateThumbnail(
            AuthUser authUser,
            Long postId, Long imageId
    );

    @Operation(
            summary = "게시물 삭제",
            description = "게시물을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 삭제 성공"),
                    @ApiResponse(responseCode = "403", description = "게시물의 생성자만이 수정을 할 수 있음 실패"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> deletePost(
            AuthUser authUser,
            Long postId
    );

    @Operation(
            summary = "게시물 즐겨찾기 삭제",
            description = "게시물 즐겨찾기를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 즐겨찾기 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "해당 게시물의 즐겨찾기가 되어있지 않음 실패"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> deleteFavorites(
            AuthUser authUser,
            Long postId
    );

    @Operation(
            summary = "게시물 이미지 삭제",
            description = "게시물 이미지를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 이미지 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "게시물에 이미지가 존재하지 않음 실패"),
                    @ApiResponse(responseCode = "400", description = "게시물에 하나 이상의 이미지 있어야 함 실패"),
                    @ApiResponse(responseCode = "403", description = "게시물의 생성자만이 수정을 할 수 있음 실패"),
                    @ApiResponse(responseCode = "404", description = "해당 ID를 가진 게시물을 찾을 수 없음 실패")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> deleteImages(
            AuthUser authUser,
            Long postId, PostImageDeleteRequest postImageDeleteRequest
    );
}
