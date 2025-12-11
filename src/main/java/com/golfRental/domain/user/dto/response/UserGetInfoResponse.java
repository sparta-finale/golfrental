package com.golfRental.domain.user.dto.response;

public record UserGetInfoResponse(
        String email,
        String username,
        String phoneNumber,
        String address,
        String nickname
) {
    public static UserGetInfoResponse from(
            String email, String username, String phoneNumber, String address, String nickname
    ) {
        return new UserGetInfoResponse(email, username, phoneNumber, address, nickname);
    }
}
