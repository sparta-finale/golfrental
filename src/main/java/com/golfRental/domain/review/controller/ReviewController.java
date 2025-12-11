package com.golfRental.domain.review.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.review.dto.request.ReviewCreateRequest;
import com.golfRental.domain.review.dto.request.ReviewUpdateRequest;
import com.golfRental.domain.review.dto.response.ReviewGetResponse;
import com.golfRental.domain.review.dto.response.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "리뷰 관리", description = "리뷰 관련 API")
public interface ReviewController {

    @Operation(
            summary = "리뷰 생성",
            description = "새로운 리뷰를 등록합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReviewResponse>> createReview(
            AuthUser authUser,
            ReviewCreateRequest request
    );

    @Operation(
            summary = "리뷰 상세 조회",
            description = "리뷰를 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReviewResponse>> getReview(
            AuthUser authUser,
            Long reviewId
    );

    @Operation(
            summary = "리뷰 조회",
            description = "유저의 리뷰를 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<ReviewGetResponse>>> getReviewsByTargetUser(
            AuthUser authUser,
            Long targetUserId,
            Pageable pageable
    );

    @Operation(
            summary = "리뷰 수정",
            description = "리뷰를 수정합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReviewResponse>> updateReview(
            AuthUser authUser,
            Long reviewId,
            ReviewUpdateRequest request
    );

    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰를 삭제합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> deleteReview(
            AuthUser authUser,
            Long reviewId
    );
}