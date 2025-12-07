package com.golfRental.domain.notification.dto.event;

import com.golfRental.domain.notification.dto.response.NotificationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private NotificationResponse notification;

    public static NotificationEvent of(Long userId, NotificationResponse notification) {
        return new NotificationEvent(userId, notification);
    }
}
