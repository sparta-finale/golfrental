package com.golfRental.domain.category.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.request.CategoryUpdateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;
import com.golfRental.domain.category.dto.response.CategoryGetAllResponse;
import com.golfRental.domain.category.dto.response.CategoryGetResponse;
import com.golfRental.domain.category.dto.response.CategoryUpdateResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryController {

    /**
     * 카테고리 생성 API (관리자 전용)
     *
     * @param categoryCreateRequest 카테고리 생성 요청 DTO
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

    /**
     * 카테고리 상세 조회 API
     *
     * @param categoryId 조회할 카테고리 ID
     * @return CategoryGetResponse
     */
    ResponseEntity<CommonApiResponse<CategoryGetResponse>>
    getCategoryById(Long categoryId);

    /**
     * 카테고리 수정 API (관리자 전용)
     *
     * @param categoryId            수정할 카테고리 ID
     * @param categoryUpdateRequest 카테고리 수정 요청 DTO
     * @return CategoryUpdateResponse
     */
    ResponseEntity<CommonApiResponse<CategoryUpdateResponse>>
    updateCategory(Long categoryId, CategoryUpdateRequest categoryUpdateRequest);

}