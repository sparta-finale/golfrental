package com.golfRental.domain.image.dto.response;

import lombok.Builder;

@Builder
public record PresignedUrlResponse(
        String presignedUrl,
        String fileUrl,
        String s3Key,
        String contentType,
        int expiresIn
) {
}
