package com.golfRental.domain.chatbot.exception;

import com.golfRental.common.exception.ErrorCode;
import com.golfRental.common.exception.GlobalException;

public class ChatbotException extends GlobalException {
    public ChatbotException(ErrorCode errorCode) {
        super(errorCode);
    }
}
