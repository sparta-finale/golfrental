package com.golfRental.domain.category.dto.response;

import lombok.Builder;

@Builder
public record CategoryGetResponse(
        Long categoryId,
        String name
) {
}