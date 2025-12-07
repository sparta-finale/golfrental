package com.golfRental.domain.notification.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.PageResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.notification.dto.request.BroadcastRequest;
import com.golfRental.domain.notification.dto.response.BroadcastResponse;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface NotificationController {

    ResponseEntity<CommonApiResponse<PageResponse<NotificationResponse>>> getNotifications(
            AuthUser authUser,
            Pageable pageable
    );

    ResponseEntity<CommonApiResponse<Void>> markAsRead(
            AuthUser authUser,
            Long notificationId
    );

    ResponseEntity<CommonApiResponse<Void>> deleteNotification(
            AuthUser authUser,
            Long notificationId
    );

    ResponseEntity<CommonApiResponse<BroadcastResponse>> broadcastNotification(
            AuthUser authUser,
            BroadcastRequest request
    );
}