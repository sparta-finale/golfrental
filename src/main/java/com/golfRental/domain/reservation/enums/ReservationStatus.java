package com.golfRental.domain.reservation.enums;

public enum ReservationStatus {
    REQUESTED,   // 예약 요청됨
    APPROVED,    // 호스트 승인
    REJECTED,    // 거절됨
    RENTED,      // 대여 중
    RETURNING,   // 반납 요청됨
    COMPLETED,   // 대여 완료
    CANCELLED// 게스트 취소
}
