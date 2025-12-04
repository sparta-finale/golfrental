package com.golfRental.domain.point.dto.response;

import lombok.Builder;

@Builder
public record PointUseResponse(
        Long pointAccountId,
        Long usedAmount,
        Long balanceAfterUse
) {
}
