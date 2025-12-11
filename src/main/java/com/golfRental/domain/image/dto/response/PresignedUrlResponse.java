package com.golfRental.domain.image.dto.response;

public record PresignedUrlResponse(
        String presignedUrl,
        String fileUrl,
        String s3Key,
        String contentType,
        int expiresIn
) {
    public static PresignedUrlResponse from(
            String presignedUrl, String fileUrl, String s3Key, String contentType, int expiresIn
    ) {
        return new PresignedUrlResponse(presignedUrl, fileUrl, s3Key, contentType, expiresIn);
    }
}
