package com.golfRental.domain.category.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;
import com.golfRental.domain.category.message.CategorySuccessMessage;
import com.golfRental.domain.category.service.command.CategoryCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryCommandService categoryCommandService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonApiResponse<CategoryCreateResponse>> createCategory(
            @RequestBody CategoryCreateRequest request
    ) {
        CategoryCreateResponse response = categoryCommandService.createCategory(request);

        return CommonApiResponse.created(
                response,
                CategorySuccessMessage.CATEGORY_CREATED
        );
    }
}
