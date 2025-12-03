package com.golfRental.domain.point.exception;

import com.golfRental.common.exception.GlobalException;

public class PointException extends GlobalException {

    public PointException(PointErrorCode pointErrorCode) {
        super(pointErrorCode);
    }
}
