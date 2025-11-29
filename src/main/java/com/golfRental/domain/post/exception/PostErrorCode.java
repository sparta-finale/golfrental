package com.golfRental.domain.post.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    // findById
    POST_INVALID_ID(HttpStatus.NOT_FOUND, "해당 ID를 가진 게시물을 찾을 수 없습니다."),

    // updatePost
    POST_NOT_EQUAL_CREATOR(HttpStatus.NOT_FOUND, "게시물의 생성자만이 수정을 할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
