package com.golfRental.domain.notification.service.command;

import com.golfRental.domain.notification.dto.request.NotificationCreateRequest;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import com.golfRental.domain.notification.entity.Notification;
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
        return NotificationResponse.from(savedNotification);
    }
}