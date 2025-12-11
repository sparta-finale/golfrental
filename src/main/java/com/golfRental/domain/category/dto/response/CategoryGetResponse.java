package com.golfRental.domain.category.dto.response;

import com.golfRental.domain.category.entity.Category;

public record CategoryGetResponse(
        Long categoryId,
        String name
) {
    public static CategoryGetResponse from(Category category) {
        return new CategoryGetResponse(
                category.getId(),
                category.getName()
        );
    }
}