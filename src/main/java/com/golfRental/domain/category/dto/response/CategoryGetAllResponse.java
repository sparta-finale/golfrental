package com.golfRental.domain.category.dto.response;

import com.golfRental.domain.category.entity.Category;

public record CategoryGetAllResponse(
        Long categoryId,
        String name
) {
    public static CategoryGetAllResponse from(Category category) {
        return new CategoryGetAllResponse(
                category.getId(),
                category.getName()
        );
    }
}
