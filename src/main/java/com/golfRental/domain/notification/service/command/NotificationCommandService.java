package com.golfRental.domain.notification.service.command;

import com.golfRental.domain.notification.dto.request.NotificationCreateRequest;
import com.golfRental.domain.notification.dto.response.NotificationResponse;

public interface NotificationCommandService {

    NotificationResponse createNotification(NotificationCreateRequest request);

    void markAsRead(Long notificationId, Long userId);
}