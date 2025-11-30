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

    // findById, getMyInfo, getInfo
    USER_INVALID_ID(HttpStatus.BAD_REQUEST, "해당 ID를 가진 유저를 찾을 수 없습니다."),

    // updateMyInfo
    USER_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    USER_DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
    USER_DUPLICATE_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 사용 중인 전화번호입니다."),

    // updatePassword
    USER_NOT_EQUAL_PASSWORD(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),
    USER_EQUAL_PASSWORD(HttpStatus.BAD_REQUEST, "수정하려는 비밀번호가 이전과 동일합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
