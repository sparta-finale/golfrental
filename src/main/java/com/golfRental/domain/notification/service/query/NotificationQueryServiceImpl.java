package com.golfRental.domain.notification.service.query;

import com.golfRental.domain.notification.dto.response.NotificationResponse;
import com.golfRental.domain.notification.entity.Notification;
import com.golfRental.domain.notification.exception.NotificationErrorCode;
import com.golfRental.domain.notification.exception.NotificationException;
import com.golfRental.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification findById(Long notificationId) {
        return notificationRepository.findByIdAndDeletedAtIsNull(notificationId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));
    }

    @Override
    public Page<NotificationResponse> findByReceiverId(Long receiverId, Pageable pageable) {
        return notificationRepository.findByReceiverIdAndDeletedAtIsNull(receiverId, pageable)
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .receiverId(notification.getReceiver().getId())
                        .title(notification.getTitle())
                        .content(notification.getContent())
                        .type(notification.getType())
                        .referenceId(notification.getReferenceId())
                        .isRead(notification.isRead())
                        .createdAt(notification.getCreatedAt())
                        .build());
    }
}