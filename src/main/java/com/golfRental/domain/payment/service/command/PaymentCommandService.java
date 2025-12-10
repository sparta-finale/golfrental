package com.golfRental.domain.payment.service.command;

import com.golfRental.domain.payment.dto.request.PaymentConfirmRequest;
import com.golfRental.domain.payment.dto.response.PaymentConfirmResponse;

public interface PaymentCommandService {
    PaymentConfirmResponse confirmPayment(PaymentConfirmRequest paymentConfirmRequest, Long userId);
}
