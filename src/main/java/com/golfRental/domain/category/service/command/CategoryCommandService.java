package com.golfRental.domain.category.service.command;

import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;

public interface CategoryCommandService {

    CategoryCreateResponse createCategory(CategoryCreateRequest request);
}
