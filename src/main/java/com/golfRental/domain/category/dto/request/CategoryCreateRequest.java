package com.golfRental.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryCreateRequest {

    @NotBlank(message = "카테고리 이름은 비어 있을 수 없습니다.")
    private String name;
}
