package com.golfRental.domain.category.dto.response;

import com.golfRental.domain.category.entity.Category;

public record CategoryUpdateResponse(
        Long categoryId,
        String name
) {
    public static CategoryUpdateResponse from(Category category) {
        return new CategoryUpdateResponse(
                category.getId(),
                category.getName()
        );
    }
}
