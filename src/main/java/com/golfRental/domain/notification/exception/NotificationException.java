package com.golfRental.domain.notification.exception;

import com.golfRental.common.exception.ErrorCode;
import com.golfRental.common.exception.GlobalException;
import lombok.Getter;

@Getter
public class NotificationException extends GlobalException {

    public NotificationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotificationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotificationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
