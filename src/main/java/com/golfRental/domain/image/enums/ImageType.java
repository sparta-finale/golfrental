package com.golfRental.domain.image.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {

    @JsonProperty("post")
    POST("posts", "게시물 이미지");

    private final String folder;
    private final String description;
}
