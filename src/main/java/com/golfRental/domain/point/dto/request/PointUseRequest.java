package com.golfRental.domain.point.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointUseRequest(

        @NotNull(message = "사용할 포인트 금액은 필수 값입니다.")
        @Min(value = 1, message = "사용할 포인트 금액은 1 이상이어야 합니다.")
        Long amount
) {
}
