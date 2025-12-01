package com.golfRental.domain.reservation.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다."),
    RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 예약에 접근할 수 없습니다."),
    RESERVATION_ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "이미 승인된 예약입니다."),
    RESERVATION_CANNOT_APPROVE(HttpStatus.BAD_REQUEST, "이 상태에서는 예약 승인할 수 없습니다."),
    RESERVATION_ALREADY_REJECTED(HttpStatus.BAD_REQUEST, "이미 거절된 예약입니다."),
    RESERVATION_CANNOT_REJECT(HttpStatus.BAD_REQUEST, "이 상태에서는 예약을 거절할 수 없습니다."),
    RESERVATION_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다."),
    RESERVATION_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "이 상태에서는 예약을 취소할 수 없습니다."),
    RESERVATION_ALREADY_RENTED(HttpStatus.BAD_REQUEST, "이미 대여가 시작된 예약입니다."),
    RESERVATION_CANNOT_START(HttpStatus.BAD_REQUEST, "이 상태에서는 대여를 시작할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}