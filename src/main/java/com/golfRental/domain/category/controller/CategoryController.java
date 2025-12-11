package com.golfRental.domain.category.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.request.CategoryUpdateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;
import com.golfRental.domain.category.dto.response.CategoryGetAllResponse;
import com.golfRental.domain.category.dto.response.CategoryGetResponse;
import com.golfRental.domain.category.dto.response.CategoryUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "카테고리 관리", description = "카테고리 관련 API")
public interface CategoryController {

    /**
     * 카테고리 생성 API (관리자 전용)
     *
     * @param categoryCreateRequest 카테고리 생성 요청 DTO
     * @return CategoryCreateResponse
     */
    @Operation(
            summary = "카테고리 생성",
            description = "관리자가 새로운 카테고리를 생성합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    ResponseEntity<CommonApiResponse<CategoryCreateResponse>>
    createCategory(CategoryCreateRequest categoryCreateRequest);

    /**
     * 카테고리 목록 조회 API
     *
     * @return List<CategoryGetAllResponse>
     */
    @Operation(
            summary = "카테고리 전체 조회",
            description = "모든 카테고리 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공")
            }
    )
    ResponseEntity<CommonApiResponse<List<CategoryGetAllResponse>>>
    getAllCategories();

    /**
     * 카테고리 상세 조회 API
     *
     * @param categoryId 조회할 카테고리 ID
     * @return CategoryGetResponse
     */
    @Operation(
            summary = "카테고리 상세 조회",
            description = "카테고리 ID로 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<CategoryGetResponse>>
    getCategoryById(Long categoryId);

    /**
     * 카테고리 수정 API (관리자 전용)
     *
     * @param categoryId            수정할 카테고리 ID
     * @param categoryUpdateRequest 카테고리 수정 요청 DTO
     * @return CategoryUpdateResponse
     */
    @Operation(
            summary = "카테고리 수정",
            description = "관리자가 카테고리 이름을 수정합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<CategoryUpdateResponse>>
    updateCategory(Long categoryId, CategoryUpdateRequest categoryUpdateRequest);

    /**
     * 카테고리 삭제 API (관리자 전용)
     *
     * @param categoryId 삭제할 카테고리 ID
     * @return void
     */
    @Operation(
            summary = "카테고리 삭제",
            description = "관리자가 카테고리를 삭제합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<Void>>
    deleteCategory(Long categoryId);
}
