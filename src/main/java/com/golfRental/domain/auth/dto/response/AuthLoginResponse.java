package com.golfRental.domain.auth.dto.response;

public record AuthLoginResponse(
        String accessToken
) {

    public static AuthLoginResponse from(String accessToken) {
        return new AuthLoginResponse(accessToken);
    }
}
