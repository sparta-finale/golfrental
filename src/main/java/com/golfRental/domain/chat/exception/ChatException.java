package com.golfRental.domain.chat.exception;

import com.golfRental.common.exception.GlobalException;

public class ChatException extends GlobalException {

    public ChatException(ChatErrorCode errorCode) {
        super(errorCode);
    }
}