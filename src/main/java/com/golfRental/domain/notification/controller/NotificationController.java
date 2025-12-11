package com.golfRental.domain.notification.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.PageResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.notification.dto.request.BroadcastRequest;
import com.golfRental.domain.notification.dto.response.BroadcastResponse;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "알림 관리", description = "알림 관련 API")
public interface NotificationController {

    @Operation(
            summary = "알림 조회",
            description = "알림을 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<PageResponse<NotificationResponse>>> getNotifications(
            AuthUser authUser,
            Pageable pageable
    );

    @Operation(
            summary = "알림 확인",
            description = "알림을 확인했습니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> markAsRead(
            AuthUser authUser,
            Long notificationId
    );

    @Operation(
            summary = "알림 삭제",
            description = "알림을 삭제했습니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> deleteNotification(
            AuthUser authUser,
            Long notificationId
    );

    @Operation(
            summary = "알림 발송",
            description = "알림을 발송했습니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<BroadcastResponse>> broadcastNotification(
            AuthUser authUser,
            BroadcastRequest request
    );
}