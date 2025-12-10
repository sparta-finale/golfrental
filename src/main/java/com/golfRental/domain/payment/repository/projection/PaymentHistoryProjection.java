package com.golfRental.domain.payment.repository.projection;

import com.golfRental.domain.payment.enums.PaymentStatus;

import java.time.LocalDateTime;

public interface PaymentHistoryProjection {

    Long getPaymentId();

    Long getAmount();

    PaymentStatus getStatus();

    LocalDateTime getCreatedAt();
}
