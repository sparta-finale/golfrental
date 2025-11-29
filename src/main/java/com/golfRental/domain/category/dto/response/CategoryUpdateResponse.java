package com.golfRental.domain.category.dto.response;

import lombok.Builder;

@Builder
public record CategoryUpdateResponse(
        Long categoryId,
        String name
) {
}