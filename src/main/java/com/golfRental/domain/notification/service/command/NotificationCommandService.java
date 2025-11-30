package com.golfRental.domain.notification.service.command;

import com.golfRental.domain.notification.dto.request.NotificationCreateRequest;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationCommandService {

    //SSE 구독 API 구현
    SseEmitter subscribe(Long userId);


    NotificationResponse createNotification(NotificationCreateRequest request);

    void markAsRead(Long notificationId, Long userId);

    void deleteNotification(Long notificationId, Long userId);
}