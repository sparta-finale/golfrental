package com.golfRental.domain.user.dto.response;

public record UserGetAllResponse(
        String email,
        String username,
        String phoneNumber,
        String address,
        String nickname
) {
    public static UserGetAllResponse create(
            String email, String username, String phoneNumber, String address, String nickname
    ) {
        return new UserGetAllResponse(email, username, phoneNumber, address, nickname);
    }
}
