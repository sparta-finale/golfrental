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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

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

    @Override
    @GetMapping("/categories")
    public ResponseEntity<CommonApiResponse<List<CategoryGetAllResponse>>> getAllCategories() {

        List<CategoryGetAllResponse> categoryGetAllResponses = categoryQueryService.getAllCategories();

        return CommonApiResponse.success(
                categoryGetAllResponses,
                CategorySuccessMessage.CATEGORY_LIST_FETCHED
        );
    }

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

    @Override
    @DeleteMapping("/admin/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonApiResponse<Void>> deleteCategory(
            @PathVariable Long categoryId
    ) {
        categoryCommandService.deleteCategory(categoryId);

        return CommonApiResponse.success(
                null,
                CategorySuccessMessage.CATEGORY_DELETED
        );
    }
}
