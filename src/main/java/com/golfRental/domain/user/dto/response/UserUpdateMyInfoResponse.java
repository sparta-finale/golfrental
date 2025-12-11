package com.golfRental.domain.user.dto.response;

public record UserUpdateMyInfoResponse(
        String email,
        String username,
        String phoneNumber,
        String address,
        String nickname
) {
    public static UserUpdateMyInfoResponse from(
            String email, String username, String phoneNumber, String address, String nickname
    ) {
        return new UserUpdateMyInfoResponse(email, username, phoneNumber, address, nickname);
    }
}
