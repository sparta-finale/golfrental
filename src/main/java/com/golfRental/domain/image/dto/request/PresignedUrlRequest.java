package com.golfRental.domain.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PresignedUrlRequest {

    @NotBlank(message = "파일명은 필수입니다.")
    private String fileName;

    @NotBlank(message = "Content Type은 필수입니다.")
    private String contentType;

    @NotBlank(message = "이미지 타입은 필수입니다.")
    @Pattern(regexp = "^(post)$",
            message = "이미지 타입은 post만 가능합니다.")
    private String type;
}
