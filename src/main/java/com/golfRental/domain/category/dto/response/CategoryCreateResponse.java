package com.golfRental.domain.category.dto.response;

import com.golfRental.domain.category.entity.Category;

public record CategoryCreateResponse(
        Long categoryId,
        String name
) {
    public static CategoryCreateResponse from(Category category) {
        return new CategoryCreateResponse(
                category.getId(),
                category.getName()
        );
    }
}
