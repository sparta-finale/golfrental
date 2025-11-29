package com.golfRental.domain.category.service.query;

import com.golfRental.domain.category.dto.response.CategoryGetAllResponse;

import java.util.List;

public interface CategoryQueryService {

    // 카테고리 목록 조회
    List<CategoryGetAllResponse> getAllCategories();
}
