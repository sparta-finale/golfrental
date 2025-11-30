package com.golfRental.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserGetAllResponse(
        String email,
        String username,
        String phoneNumber,
        String address,
        String nickname
) {
}
