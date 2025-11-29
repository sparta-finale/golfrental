package com.golfRental.domain.category.service.query;

import com.golfRental.domain.category.dto.response.CategoryGetAllResponse;
import com.golfRental.domain.category.dto.response.CategoryGetResponse;
import com.golfRental.domain.category.entity.Category;

import java.util.List;

public interface CategoryQueryService {

    // 카테고리 목록 조회
    List<CategoryGetAllResponse> getAllCategories();

    // 카테고리 상세 조회 (Response DTO)
    CategoryGetResponse getCategory(Long categoryId);

    // 다른 도메인 연동용 엔티티 조회
    Category findById(Long categoryId);
}