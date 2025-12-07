package com.golfRental.domain.image.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {

    IMAGE_INVALID_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 이미지 타입입니다."),
    IMAGE_INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 파일명입니다."),
    IMAGE_INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 Content-Type입니다."),
    IMAGE_INVALID_PRIMARY_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 Primary-Type입니다."),
    IMAGE_INVALID_SUB_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 Sub-Type입니다."),
    IMAGE_EXTENSION_SUB_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "확장자와 Sub-Type이 일치하지 않습니다."),
    IMAGE_PRESIGNED_URL_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Presigned URL 생성에 실패했습니다."),
    IMAGE_INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
