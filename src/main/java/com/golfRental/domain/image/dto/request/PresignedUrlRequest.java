package com.golfRental.domain.image.dto.request;

import com.golfRental.domain.image.enums.ImageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PresignedUrlRequest {

    @NotBlank(message = "파일명은 필수입니다.")
    private String fileName;

    @NotBlank(message = "Content Type은 필수입니다.")
    private String contentType;

    @NotNull(message = "이미지 타입은 필수입니다.(현재는 post만 가능)")
    private ImageType type;
}
