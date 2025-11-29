package com.golfRental.domain.category.service.command;

import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.request.CategoryUpdateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;
import com.golfRental.domain.category.dto.response.CategoryUpdateResponse;


public interface CategoryCommandService {

    CategoryCreateResponse createCategory(CategoryCreateRequest request);

    CategoryUpdateResponse updateCategory(Long categoryId, CategoryUpdateRequest request);
}
