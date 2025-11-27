package com.golfRental.domain.post.exception;

import com.golfRental.common.exception.GlobalException;

public class PostException extends GlobalException {

    public PostException(PostErrorCode postErrorCode) {
        super(postErrorCode);
    }
}
