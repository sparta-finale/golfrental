package com.golfRental.domain.image.dto.request;

import com.golfRental.domain.image.enums.ImageType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImageSaveRequest(
        @NotBlank(message = "파일명은 필수입니다.")
        String fileName,

        @NotBlank(message = "URL은 필수입니다.")
        String url,

        @NotBlank(message = "S3 Key는 필수입니다.")
        String s3Key,

        @NotBlank(message = "Content Type은 필수입니다.")
        String contentType,

        @NotNull(message = "파일 크기는 필수입니다.")
        @Min(value = 1, message = "파일 크기는 1바이트 이상이어야 합니다.")
        Long size,

        @NotNull(message = "이미지 타입은 필수입니다.(현재는 post만 가능)")
        ImageType type
) {
}
