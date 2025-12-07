package com.golfRental.domain.payment.enums;

public enum PaymentStatus {
    REQUESTED,   // 결제 요청됨
    SUCCESS,     // 결제 성공
    FAILED,      // 결제 실패
    CANCELED     // 결제 취소됨
}