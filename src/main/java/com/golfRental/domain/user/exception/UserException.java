package com.golfRental.domain.user.exception;

import com.golfRental.common.exception.GlobalException;

public class UserException extends GlobalException {

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode);
    }
}
