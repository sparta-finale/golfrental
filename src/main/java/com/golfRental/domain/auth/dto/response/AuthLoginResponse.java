package com.golfRental.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthLoginResponse(
        String accessToken
) {
}
