package com.golfRental.domain.payment.dto.response;

import com.golfRental.domain.payment.enums.PaymentStatus;
import com.golfRental.domain.payment.repository.projection.PaymentHistoryProjection;

import java.time.LocalDateTime;

public record PaymentHistoryResponse(
        Long paymentId,
        Long amount,
        PaymentStatus status,
        String method,
        LocalDateTime createdAt
) {
    public static PaymentHistoryResponse fromProjection(PaymentHistoryProjection p) {
        return new PaymentHistoryResponse(
                p.getPaymentId(),
                p.getAmount(),
                p.getStatus(),
                "TOSS",
                p.getCreatedAt()
        );
    }
}