package com.golfRental.domain.image.dto.response;

import com.golfRental.domain.image.entity.Image;

public record ImageSavedResponse(
        Long id,
        String fileName,
        String url,
        String s3Key,
        String contentType,
        Long size,
        String type
) {
    public static ImageSavedResponse from(Image image) {
        return new ImageSavedResponse(
                image.getId(), image.getFileName(), image.getUrl(), image.getS3Key(),
                image.getContentType(), image.getSize(), image.getType().toString().toLowerCase()
        );
    }
}
