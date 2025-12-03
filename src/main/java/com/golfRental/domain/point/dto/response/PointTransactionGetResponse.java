package com.golfRental.domain.point.dto.response;

import com.golfRental.domain.point.enums.PointTransactionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PointTransactionGetResponse(
        Long transactionId,
        Integer amount,
        PointTransactionType type,
        Integer balanceAfter,
        LocalDateTime createdAt
) {
}
