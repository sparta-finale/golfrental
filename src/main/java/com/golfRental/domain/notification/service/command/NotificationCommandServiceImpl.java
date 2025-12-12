package com.golfRental.domain.notification.service.command;

import com.golfRental.domain.notification.dto.event.NotificationEvent;
import com.golfRental.domain.notification.dto.request.BroadcastRequest;
import com.golfRental.domain.notification.dto.request.NotificationCreateRequest;
import com.golfRental.domain.notification.dto.response.BroadcastResponse;
import com.golfRental.domain.notification.dto.response.NotificationResponse;
import com.golfRental.domain.notification.entity.Notification;
import com.golfRental.domain.notification.enums.NotificationType;
import com.golfRental.domain.notification.exception.NotificationErrorCode;
import com.golfRental.domain.notification.exception.NotificationException;
import com.golfRental.domain.notification.publisher.NotificationRedisPublisher;
import com.golfRental.domain.notification.repository.NotificationRepository;
import com.golfRental.domain.notification.repository.SseEmitterRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private static final Long DEFAULT_TIMEOUT = 300_000L; //5분
    private final NotificationRepository notificationRepository;
    private final UserQueryService userQueryService;
    private final SseEmitterRepository sseEmitterRepository;

    private final NotificationRedisPublisher notificationRedisPublisher;

    @Override
    public SseEmitter subscribe(Long userId) {

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        sseEmitterRepository.save(userId, emitter);

        //연결 종료 시 처리(완료, 타임아웃, 연결에러)
        emitter.onCompletion(() -> {
            sseEmitterRepository.deleteById(userId);
        });

        emitter.onTimeout(() -> {
            sseEmitterRepository.deleteById(userId);
        });

        emitter.onError((e) -> {
            log.warn("SSE emitter에 오류가 발생했습니다. userId={}", userId, e);
            sseEmitterRepository.deleteById(userId);
        });

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected to SSE"));
        } catch (Exception e) {
            sseEmitterRepository.deleteById(userId);
            throw new NotificationException(NotificationErrorCode.SSE_CONNECTION_ERROR);
        }
        return emitter;
    }

    private void sendNotification(Long userId, NotificationResponse notification) {
        SseEmitter emitter = sseEmitterRepository.get(userId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification));
            } catch (Exception e) {
                log.warn("알림 전송에 실패했습니다. userId={}", userId, e);
                sseEmitterRepository.deleteById(userId);
            }
        }
    }


    @Override
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        User receiver = userQueryService.findById(request.getReceiverId());
        Notification notification = request.toEntity(receiver);
        Notification savedNotification = notificationRepository.save(notification);

        NotificationResponse response = NotificationResponse.builder()
                .id(savedNotification.getId())
                .receiverId(savedNotification.getReceiver().getId())
                .title(savedNotification.getTitle())
                .content(savedNotification.getContent())
                .type(savedNotification.getType())
                .referenceId(savedNotification.getReferenceId())
                .isRead(savedNotification.isRead())
                .createdAt(savedNotification.getCreatedAt())
                .build();

        try {
            NotificationEvent event = NotificationEvent.of(request.getReceiverId(), response);
            notificationRedisPublisher.publish(event);
            log.info("Redis 알림 발행 성공 - notificationId: {}", savedNotification.getId());
        } catch (Exception e) {
            log.error("Redis 알림 발행 실패 - notificationId: {}, userId: {}",
                    savedNotification.getId(), request.getReceiverId(), e);
            // Redis 실패해도 알림은 DB에 저장됨 (DB 우선)
        }

        return response;
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

    @Override
    public BroadcastResponse broadcastNotification(BroadcastRequest request) {
        List<User> allUsers = userQueryService.findAll();
        int totalUsers = allUsers.size();

// 2. Notification 엔티티 리스트 생성 (N+1 제거: User 객체 직접 사용)
        List<Notification> notifications = allUsers.stream()
                .map(user -> Notification.builder()
                        .receiver(user)  // User 객체 직접 사용 (재조회 없음!)
                        .title(request.getTitle())
                        .content(request.getContent())
                        .type(NotificationType.SYSTEM)
                        .referenceId(null)
                        .build())
                .toList();

        List<Notification> savedNotifications = notificationRepository.saveAll(notifications);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        savedNotifications.parallelStream().forEach(notification -> {
            try {
                // NotificationResponse 생성 (팩토리 메서드 사용)
                NotificationResponse response = NotificationResponse.from(notification);

                // Redis 발행
                NotificationEvent event = NotificationEvent.of(
                        notification.getReceiver().getId(),
                        response
                );
                notificationRedisPublisher.publish(event);
                successCount.incrementAndGet();

            } catch (Exception e) {
                log.error("Redis 발행 실패 - notificationId: {}, userId: {}",
                        notification.getId(),
                        notification.getReceiver().getId(),
                        e);
                failCount.incrementAndGet();
                // Redis 실패해도 DB에는 저장됨 (DB 우선)
            }
        });

        log.info("관리자 공지 발송 완료 - 총: {}, 성공: {}, 실패: {}",
                totalUsers, successCount, failCount);

        return BroadcastResponse.of(totalUsers, successCount.get(), failCount.get());
    }

//    public void broadcast(NotificationDto notification) {
//        if (emitters.isEmpty()) {
//            log.debug("SSE 연결 없음, 브로드캐스트 전송 스킵 - type: {}", notification.type());
//            return;
//        }
//
//        int successCount = 0;
//        int failCount = 0;
//
//        // Effective Java Item 81: ConcurrentHashMap.entrySet()는 WeaklyConsistent Iterator 제공
//        // → 반복 중 수정 가능, ConcurrentModificationException 없음
//        for (Map.Entry<UUID, SseEmitter> entry : emitters.entrySet()) {
//            UUID userPublicId = entry.getKey();
//            SseEmitter emitter = entry.getValue();
//
//            try {
//                synchronized (emitter) {  // Spring 공식 권장: send() 호출 시 동기화
//                    emitter.send(SseEmitter.event()
//                            .name(SseEventType.NOTIFICATION.getValue())
//                            .data(notification));
//                }
//                successCount++;
//            } catch (IOException e) {
//                // 네트워크 오류 → completeWithError() 호출 (Spring 공식 권장)
//                log.warn("SSE 브로드캐스트 실패 (네트워크 오류) - userPublicId: {}, type: {}",
//                        userPublicId, notification.type());
//                removeEmitterSafely(userPublicId, "브로드캐스트 실패 (네트워크 오류)", e);
//                failCount++;
//            } catch (IllegalStateException e) {
//                // 이미 종료된 emitter (Race Condition 정상 시나리오)
//                log.debug("SSE 브로드캐스트 실패 (이미 종료됨) - userPublicId: {}", userPublicId);
//                removeEmitterSafely(userPublicId, "브로드캐스트 실패 (이미 종료됨)");
//                failCount++;
//            }
//        }
//
//        log.info("SSE 브로드캐스트 완료 - type: {}, 성공: {}, 실패: {}",
//                notification.type(), successCount, failCount);
//    }
//
//
//
//
//
//    강준호(Spring_8기) has paused their notifications
//
}