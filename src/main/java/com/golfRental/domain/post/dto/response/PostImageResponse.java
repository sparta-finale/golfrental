package com.golfRental.domain.post.dto.response;

public record PostImageResponse(
        String url,
        Boolean isThumbnail,
        Integer sortOrder
) {
    public static PostImageResponse create(String url, Boolean isThumbnail, Integer sortOrder) {
        return new PostImageResponse(url, isThumbnail, sortOrder);
    }
}
