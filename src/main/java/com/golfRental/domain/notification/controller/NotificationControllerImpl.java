package com.golfRental.domain.notification.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.PageResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import com.golfRental.domain.notification.service.command.NotificationCommandService;
import com.golfRental.domain.notification.service.query.NotificationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {

    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;

    @Override
    @GetMapping
    public ResponseEntity<CommonApiResponse<PageResponse<NotificationResponse>>> getNotifications(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<NotificationResponse> response = notificationQueryService.findByReceiverId(authUser.getUserId(), pageable);
        return CommonApiResponse.pageSuccess(response, "알림 목록 조회 성공");
    }

    @Override
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<CommonApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long notificationId
    ) {
        notificationCommandService.markAsRead(notificationId, authUser.getUserId());
        return CommonApiResponse.success(null, "알림 읽음 성공");
    }

    @Override
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<CommonApiResponse<Void>> deleteNotification(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long notificationId
    ) {
        notificationCommandService.deleteNotification(notificationId, authUser.getUserId());
        return CommonApiResponse.deleteSuccess("알림 삭제 성공");
    }
}
