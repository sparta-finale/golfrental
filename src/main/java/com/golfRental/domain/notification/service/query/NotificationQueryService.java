package com.golfRental.domain.notification.service.query;

import com.golfRental.domain.notification.dto.response.NotificationResponse;
import com.golfRental.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationQueryService {

    Notification findById(Long notificationId);

    Page<NotificationResponse> findByReceiverId(Long receiverId, Pageable pageable);
}