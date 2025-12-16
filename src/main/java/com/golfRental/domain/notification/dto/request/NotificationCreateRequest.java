package com.golfRental.domain.notification.dto.request;

import com.golfRental.domain.notification.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationCreateRequest(
        @NotNull(message = "수신자 ID는 필수입니다.")
        Long receiverId,

        @NotBlank(message = "알림 제목은 필수입니다.")
        @Size(max = 100, message = "알림 제목은 100자 이하여야 합니다.")
        String title,

        @NotBlank(message = "알림 내용은 필수입니다.")
        @Size(max = 500, message = "알림 내용은 500자 이하여야 합니다.")
        String content,

        @NotNull(message = "알림 타입은 필수입니다.")
        NotificationType type,

        Long referenceId
) {
    public static NotificationCreateRequest of(Long receiverId, String title, String content, NotificationType type, Long referenceId) {
        return new NotificationCreateRequest(receiverId, title, content, type, referenceId);
    }

    public com.golfRental.domain.notification.entity.Notification toEntity(com.golfRental.domain.user.entity.User receiver) {
        return com.golfRental.domain.notification.entity.Notification.builder()
                .receiver(receiver)
                .title(title)
                .content(content)
                .type(type)
                .referenceId(referenceId)
                .build();
    }
}