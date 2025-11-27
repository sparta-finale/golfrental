package com.golfRental.domain.user.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // findByEmail
    USER_INVALID_EMAIL(HttpStatus.BAD_REQUEST, "해당 이메일을 가진 유저를 찾을 수 없습니다."),

    // findById, getMyInfo
    USER_INVALID_ID(HttpStatus.BAD_REQUEST, "해당 ID를 가진 유저를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
