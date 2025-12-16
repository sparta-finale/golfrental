package com.golfRental.domain.notification.dto.event;

import com.golfRental.domain.notification.dto.response.NotificationResponse;

import java.io.Serializable;

public record NotificationEvent(
        Long userId,
        NotificationResponse notification
) implements Serializable {
    private static final long serialVersionUID = 1L;


    public static NotificationEvent of(Long userId, NotificationResponse notification) {
        return new NotificationEvent(userId, notification);
    }
}
