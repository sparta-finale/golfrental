package com.golfRental.domain.point.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PointErrorCode implements ErrorCode {

    POINT_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "포인트 계좌를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}