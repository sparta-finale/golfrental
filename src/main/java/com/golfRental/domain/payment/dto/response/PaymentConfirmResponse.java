package com.golfRental.domain.payment.dto.response;


import com.golfRental.domain.payment.entity.Payment;
import com.golfRental.domain.payment.enums.PaymentStatus;

public record PaymentConfirmResponse(
        Long paymentId,
        String orderId,
        String paymentKey,
        Long amount,
        PaymentStatus status
) {
    public static PaymentConfirmResponse from(Payment payment) {
        return new PaymentConfirmResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getPaymentKey(),
                payment.getAmount(),
                payment.getStatus()
        );
    }
}
