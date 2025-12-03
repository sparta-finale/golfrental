package com.golfRental.domain.chat.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지를 찾을 수 없습니다."),
    CHAT_ROOM_FORBIDDEN(HttpStatus.FORBIDDEN, "채팅방에 대한 권한이 없습니다."),
    CHAT_ROOM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "해당 예약에 대한 채팅방이 이미 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}