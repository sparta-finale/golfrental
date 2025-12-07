package com.golfRental.domain.image.dto.response;

import lombok.Builder;

@Builder
public record ImageSavedResponse(
        Long id,
        String fileName,
        String url,
        String s3Key,
        String contentType,
        Long size,
        String type
) {
}
