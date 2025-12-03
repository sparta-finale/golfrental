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
    RESERVATION_CANNOT_START(HttpStatus.BAD_REQUEST, "이 상태에서는 대여를 시작할 수 없습니다."),
    RESERVATION_ALREADY_RETURNING(HttpStatus.BAD_REQUEST, "이미 반납 요청된 예약입니다."),
    RESERVATION_CANNOT_REQUEST_RETURN(HttpStatus.BAD_REQUEST, "이 상태에서는 반납 요청을 할 수 없습니다."),
    RESERVATION_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 반납 완료된 예약입니다."),
    RESERVATION_CANNOT_COMPLETE(HttpStatus.BAD_REQUEST, "이 상태에서는 반납 승인할 수 없습니다."),
    RESERVATION_SELF_BOOKING_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자신의 게시글은 예약할 수 없습니다."),
    RESERVATION_INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "예약 종료일은 시작일보다 이후여야 합니다."),
    RESERVATION_POST_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "예약할 수 없는 상태의 게시글입니다."),
    RESERVATION_DATE_CONFLICT(HttpStatus.BAD_REQUEST, "해당 기간에 이미 예약이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}