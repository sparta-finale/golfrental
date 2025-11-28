package com.golfRental.domain.category.dto.response;

import lombok.Builder;

@Builder
public record CategoryCreateResponse(
        Long categoryId,
        String name
) {
}
