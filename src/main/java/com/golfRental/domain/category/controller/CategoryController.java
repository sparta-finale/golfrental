package com.golfRental.domain.category.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;
import com.golfRental.domain.category.dto.response.CategoryGetAllResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryController {

    /**
     * 카테고리 생성 API
     *
     * @param categoryCreateRequest
     * @return CategoryCreateResponse
     */
    ResponseEntity<CommonApiResponse<CategoryCreateResponse>>
    createCategory(CategoryCreateRequest categoryCreateRequest);

    /**
     * 카테고리 목록 조회 API
     *
     * @return List<CategoryGetAllResponse>
     */
    ResponseEntity<CommonApiResponse<List<CategoryGetAllResponse>>>
    getAllCategories();
}