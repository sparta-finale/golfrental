package com.golfRental.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(

        @NotBlank(message = "카테고리 이름은 비어 있을 수 없습니다.")
        String name

) {
}