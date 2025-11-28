package com.golfRental.domain.notification.dto.request;

import com.golfRental.domain.notification.entity.Notification;
import com.golfRental.domain.notification.enums.NotificationType;
import com.golfRental.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationCreateRequest {

    @NotNull(message = "수신자 ID는 필수입니다.")
    private Long receiverId;

    @NotBlank(message = "알림 제목은 필수입니다.")
    @Size(max = 100, message = "알림 제목은 100자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "알림 내용은 필수입니다.")
    @Size(max = 500, message = "알림 내용은 500자 이하여야 합니다.")
    private String content;

    @NotNull(message = "알림 타입은 필수입니다.")
    private NotificationType type;

    private Long referenceId;

    public Notification toEntity(User receiver) {
        return Notification.builder()
                .receiver(receiver)
                .title(title)
                .content(content)
                .type(type)
                .referenceId(referenceId)
                .build();
    }
}