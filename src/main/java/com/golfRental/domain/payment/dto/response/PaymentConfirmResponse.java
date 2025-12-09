package com.golfRental.domain.payment.dto.response;


import com.golfRental.domain.payment.enums.PaymentStatus;

public record PaymentConfirmResponse(
        Long paymentId,
        String orderId,
        String paymentKey,
        Long amount,
        PaymentStatus status
) {
}
