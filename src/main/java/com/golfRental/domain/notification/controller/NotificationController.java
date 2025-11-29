package com.golfRental.domain.notification.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface NotificationController {

    ResponseEntity<CommonApiResponse<Page<NotificationResponse>>> getNotifications(
            AuthUser authUser,
            Pageable pageable
    );
}