package com.golfRental.domain.payment.dto.response;

import com.golfRental.domain.payment.entity.Payment;
import com.golfRental.domain.payment.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentHistoryResponse(
        Long paymentId,
        Long amount,
        PaymentStatus status,
        String method,
        LocalDateTime createdAt
) {
    public static PaymentHistoryResponse from(Payment payment) {
        return new PaymentHistoryResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getStatus(),
                "TOSS", // 현재 단일 결제 방식
                payment.getCreatedAt()
        );
    }
}