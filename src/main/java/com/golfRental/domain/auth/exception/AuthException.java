package com.golfRental.domain.auth.exception;

import com.golfRental.common.exception.GlobalException;

public class AuthException extends GlobalException {

    public AuthException(AuthErrorCode authErrorCode) {
        super(authErrorCode);
    }
}
