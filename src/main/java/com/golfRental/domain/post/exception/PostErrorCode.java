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
    POST_NOT_EQUAL_CREATOR(HttpStatus.FORBIDDEN, "게시물의 생성자만이 수정을 할 수 있습니다."),

    // addFavorites
    POST_DUPLICATION_FAVORITES(HttpStatus.BAD_REQUEST, "이미 해당 게시물의 즐겨찾기 추가가 되어있습니다."),

    // deleteFavorites
    POST_NOT_FAVORITES(HttpStatus.BAD_REQUEST, "이미 해당 게시물의 즐겨찾기 되어있지 않습니다."),

    // createPost
    DUPLICATE_IMAGE_IDS(HttpStatus.BAD_REQUEST, "중복된 이미지 ID가 포함되어 있습니다."),

    // updateThumbnail
    POST_IMAGE_NOT_EXIST(HttpStatus.BAD_REQUEST, "게시물에 이미지가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
