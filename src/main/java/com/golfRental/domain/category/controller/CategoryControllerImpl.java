package com.golfRental.domain.category.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.request.CategoryUpdateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;
import com.golfRental.domain.category.dto.response.CategoryGetAllResponse;
import com.golfRental.domain.category.dto.response.CategoryGetResponse;
import com.golfRental.domain.category.dto.response.CategoryUpdateResponse;
import com.golfRental.domain.category.message.CategorySuccessMessage;
import com.golfRental.domain.category.service.command.CategoryCommandService;
import com.golfRental.domain.category.service.query.CategoryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "카테고리 관리", description = "카테고리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

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
    @Override
    @PostMapping("/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonApiResponse<CategoryCreateResponse>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        CategoryCreateResponse categoryCreateResponse = categoryCommandService.createCategory(request);
        return CommonApiResponse.created(
                categoryCreateResponse,
                CategorySuccessMessage.CATEGORY_CREATED
        );
    }

    @Operation(
            summary = "카테고리 전체 조회",
            description = "모든 카테고리 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공")
            }
    )
    @Override
    @GetMapping("/categories")
    public ResponseEntity<CommonApiResponse<List<CategoryGetAllResponse>>> getAllCategories() {

        List<CategoryGetAllResponse> categoryGetAllResponses = categoryQueryService.getAllCategories();

        return CommonApiResponse.success(
                categoryGetAllResponses,
                CategorySuccessMessage.CATEGORY_LIST_FETCHED
        );
    }

    @Operation(
            summary = "카테고리 상세 조회",
            description = "카테고리 ID를 기반으로 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
            }
    )
    @Override
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CommonApiResponse<CategoryGetResponse>> getCategoryById(
            @PathVariable Long categoryId
    ) {
        CategoryGetResponse categoryGetResponse = categoryQueryService.getCategory(categoryId);

        return CommonApiResponse.success(
                categoryGetResponse,
                CategorySuccessMessage.CATEGORY_GET_SUCCESS
        );
    }

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
    @Override
    @PutMapping("/admin/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonApiResponse<CategoryUpdateResponse>> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {
        CategoryUpdateResponse response = categoryCommandService.updateCategory(categoryId, request);

        return CommonApiResponse.success(
                response,
                CategorySuccessMessage.CATEGORY_UPDATED
        );
    }

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
    @Override
    @DeleteMapping("/admin/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonApiResponse<Void>> deleteCategory(
            @PathVariable Long categoryId
    ) {
        categoryCommandService.deleteCategory(categoryId);

        return CommonApiResponse.deleteSuccess(
                CategorySuccessMessage.CATEGORY_DELETED
        );
    }
}
