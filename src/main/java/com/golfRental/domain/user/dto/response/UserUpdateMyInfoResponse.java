package com.golfRental.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserUpdateMyInfoResponse(
        String email,
        String name,
        String phoneNumber,
        String address,
        String nickname
) {
}
