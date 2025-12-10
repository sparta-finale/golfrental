package com.golfRental.domain.payment.exception;

import com.golfRental.common.exception.GlobalException;

public class PaymentException extends GlobalException {

    public PaymentException(PaymentErrorCode paymentErrorCode) {
        super(paymentErrorCode);
    }
}
