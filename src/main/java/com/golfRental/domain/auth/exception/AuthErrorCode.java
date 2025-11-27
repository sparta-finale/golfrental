package com.golfRental.domain.auth.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // Signup
    AUTH_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    AUTH_DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    AUTH_DUPLICATE_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 사용 중인 전화번호입니다."),

    // Login
    AUTH_INVALID_EMAIL(HttpStatus.BAD_REQUEST, "잘못된 이메일입니다."),
    AUTH_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
