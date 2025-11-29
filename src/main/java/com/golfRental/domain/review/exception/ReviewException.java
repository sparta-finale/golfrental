package com.golfRental.domain.review.exception;

import com.golfRental.common.exception.GlobalException;

public class ReviewException extends GlobalException {

    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode);
    }
}