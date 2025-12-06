package com.golfRental.domain.image.exception;

import com.golfRental.common.exception.GlobalException;

public class ImageException extends GlobalException {

    public ImageException(ImageErrorCode imageErrorCode) {
        super(imageErrorCode);
    }
}
