package com.golfRental.domain.post.dto.request;

import java.util.List;

public record PostImageDeleteRequest(
        List<Long> imageIds
) {
}
