package com.golfRental.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserGetMyInfoResponse(
        String email,
        String name,
        String phoneNumber,
        String address,
        String nickname
) {
}
