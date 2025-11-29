package com.golfRental.domain.review.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    // 404 NOT FOUND
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),

    // 400 BAD REQUEST
    RESERVATION_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "완료된 예약에 대해서만 리뷰를 작성할 수 있습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "해당 예약에 대한 리뷰가 이미 존재합니다."),
    INVALID_REVIEW_AUTHOR(HttpStatus.BAD_REQUEST, "예약 참여자만 리뷰를 작성할 수 있습니다."),

    // 403 FORBIDDEN
    REVIEW_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰에 대한 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}