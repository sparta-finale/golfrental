package com.golfRental.domain.notification.service.command;

import com.golfRental.domain.notification.dto.request.NotificationCreateRequest;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import com.golfRental.domain.notification.entity.Notification;
import com.golfRental.domain.notification.exception.NotificationErrorCode;
import com.golfRental.domain.notification.exception.NotificationException;
import com.golfRental.domain.notification.repository.NotificationRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final UserQueryService userQueryService;

    @Override
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        User receiver = userQueryService.findById(request.getReceiverId());
        Notification notification = request.toEntity(receiver);
        Notification savedNotification = notificationRepository.save(notification);

        return NotificationResponse.builder()
                .id(savedNotification.getId())
                .receiverId(savedNotification.getReceiver().getId())
                .title(savedNotification.getTitle())
                .content(savedNotification.getContent())
                .type(savedNotification.getType())
                .referenceId(savedNotification.getReferenceId())
                .isRead(savedNotification.isRead())
                .createdAt(savedNotification.getCreatedAt())
                .build();
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = findByIdWithAccessCheck(notificationId, userId);

        notification.markAsRead();
    }

    @Override
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = findByIdWithAccessCheck(notificationId, userId);
        notification.delete();
    }


    //조회 권한메서드
    /*
     * markAsRead와 deleteNotification에서 반복 사용되는 부분을 메서드화 하였음
     */
    private Notification findByIdWithAccessCheck(Long notificationId, Long userId) {
        Notification notification = notificationRepository
                .findByIdAndDeletedAtIsNull(notificationId)
                .orElseThrow(() -> new NotificationException(
                        NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getReceiver().getId().equals(userId)) {
            throw new NotificationException(
                    NotificationErrorCode.NOTIFICATION_ACCESS_DENIED);
        }

        return notification;
    }
}