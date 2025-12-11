package com.golfRental.domain.point.dto.response;

import com.golfRental.domain.point.entity.PointAccount;

public record PointBalanceResponse(
        Long pointAccountId,
        Long balance
) {

    public static PointBalanceResponse from(PointAccount account) {
        return new PointBalanceResponse(
                account.getId(),
                account.getBalance()
        );
    }
}
