package com.golfRental.domain.point.dto.response;

import com.golfRental.domain.point.entity.PointAccount;

public record PointUseResponse(
        Long pointAccountId,
        Long usedAmount,
        Long balanceAfterUse
) {

    public static PointUseResponse from(PointAccount account, Long usedAmount) {
        return new PointUseResponse(
                account.getId(),
                usedAmount,
                account.getBalance()
        );
    }
}
