package com.golfRental.domain.payment.repository.projection;

import java.time.LocalDateTime;

public interface PaymentHistoryProjection {
    Long getPaymentId();

    Long getAmount();

    String getStatus();

    String getMethod();

    LocalDateTime getCreatedAt();
}
