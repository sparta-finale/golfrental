package com.golfRental.domain.point.dto.response;

import com.golfRental.domain.point.entity.PointTransaction;
import com.golfRental.domain.point.enums.PointTransactionType;

import java.time.LocalDateTime;

public record PointTransactionGetResponse(
        Long transactionId,
        Long amount,
        PointTransactionType type,
        Long balanceAfter,
        LocalDateTime createdAt
) {

    public static PointTransactionGetResponse from(PointTransaction transaction) {
        return new PointTransactionGetResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getBalanceAfter(),
                transaction.getCreatedAt()
        );
    }
}