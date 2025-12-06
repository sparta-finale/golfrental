package com.golfRental.domain.image.enums;

import com.golfRental.domain.image.exception.ImageErrorCode;
import com.golfRental.domain.image.exception.ImageException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {

    POST("posts", "게시물 이미지");

    private final String folder;
    private final String description;

    public static ImageType fromString(String type) {
        try {
            return ImageType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ImageException(ImageErrorCode.IMAGE_INVALID_TYPE);
        }
    }
}
