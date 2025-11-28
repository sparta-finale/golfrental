package com.golfRental.domain.category.service.command;

import com.golfRental.domain.category.dto.request.CategoryCreateRequest;
import com.golfRental.domain.category.dto.response.CategoryCreateResponse;
import com.golfRental.domain.category.entity.Category;
import com.golfRental.domain.category.exception.CategoryErrorCode;
import com.golfRental.domain.category.exception.CategoryException;
import com.golfRental.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {

        if (categoryRepository.existsByNameAndNotDeleted(request.getName())) {
            throw new CategoryException(CategoryErrorCode.CATEGORY_DUPLICATED_NAME);
        }

        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category saved = categoryRepository.save(category);

        return CategoryCreateResponse.builder()
                .categoryId(saved.getId())
                .name(saved.getName())
                .build();
    }
}
