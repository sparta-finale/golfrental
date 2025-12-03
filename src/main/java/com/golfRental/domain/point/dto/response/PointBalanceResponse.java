package com.golfRental.domain.point.dto.response;

import lombok.Builder;

@Builder
public record PointBalanceResponse(
        Long pointAccountId,
        Integer balance
) {
}
