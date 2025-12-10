package com.golfRental.domain.payment.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

    PAYMENT_CONFIRM_FAILED(HttpStatus.BAD_REQUEST, "결제 승인이 실패했습니다."),
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "결제 금액이 올바르지 않습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),
    PAYMENT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "결제 검증에 실패했습니다."),
    PAYMENT_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "결제 서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
