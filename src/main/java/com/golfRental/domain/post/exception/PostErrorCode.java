package com.golfRental.domain.post.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    // findById
    POST_INVALID_ID(HttpStatus.BAD_REQUEST, "해당 ID를 가진 게시물을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
