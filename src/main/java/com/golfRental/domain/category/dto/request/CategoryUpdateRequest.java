package com.golfRental.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequest {

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;

    public CategoryUpdateRequest(String name) {
        this.name = name;
    }
}