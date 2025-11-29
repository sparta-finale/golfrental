package com.golfRental.domain.category.service.query;

import com.golfRental.domain.category.dto.response.CategoryGetAllResponse;
import com.golfRental.domain.category.dto.response.CategoryGetResponse;
import com.golfRental.domain.category.entity.Category;
import com.golfRental.domain.category.exception.CategoryErrorCode;
import com.golfRental.domain.category.exception.CategoryException;
import com.golfRental.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryGetAllResponse> getAllCategories() {

        List<Category> categories =
                categoryRepository.findAllByDeletedAtIsNullOrderByNameAsc();

        return categories.stream()
                .map(category ->
                        CategoryGetAllResponse.builder()
                                .categoryId(category.getId())
                                .name(category.getName())
                                .build()
                )
                .toList();
    }

    @Override
    public CategoryGetResponse getCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        return CategoryGetResponse.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }
}