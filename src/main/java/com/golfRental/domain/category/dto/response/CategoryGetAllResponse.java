package com.golfRental.domain.category.dto.response;

import lombok.Builder;

@Builder
public record CategoryGetAllResponse(
        Long categoryId,
        String name
) {
}
