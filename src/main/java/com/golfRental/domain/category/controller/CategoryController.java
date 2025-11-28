package com.golfRental.domain.category.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryController {

    ResponseEntity<CommonApiResponse<CategoryCreateResponse>> createCategory(CategoryCreateRequest categoryCreateRequest);
}
