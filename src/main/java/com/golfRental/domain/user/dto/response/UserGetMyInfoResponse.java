package com.golfRental.domain.user.dto.response;

public record UserGetMyInfoResponse(
        String email,
        String username,
        String phoneNumber,
        String address,
        String nickname
) {
    public static UserGetMyInfoResponse from(
            String email, String username, String phoneNumber, String address, String nickname
    ) {
        return new UserGetMyInfoResponse(email, username, phoneNumber, address, nickname);
    }
}
