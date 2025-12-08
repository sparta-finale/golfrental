package com.golfRental.domain.post.dto.response;

import lombok.Builder;

@Builder
public record PostImageResponse(
        String url,
        Boolean isThumbnail,
        Integer sortOrder
) {
}
