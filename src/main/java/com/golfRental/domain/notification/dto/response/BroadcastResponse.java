package com.golfRental.domain.notification.dto.response;

import lombok.Builder;

@Builder
public record BroadcastResponse(
        int totalUsers,
        int successCount,
        int failCount
) {
    public static BroadcastResponse of(int totalUsers, int successCount, int failCount) {
        return new BroadcastResponse(totalUsers, successCount, failCount);
    }
}