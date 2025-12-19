package com.golfRental.domain.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golfRental.domain.notification.dto.event.NotificationEvent;
import com.golfRental.domain.notification.repository.SseEmitterRepository;
import com.golfRental.domain.notification.service.command.NotificationCommandService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRedisSubscriber implements MessageListener {

    private static final String NOTIFICATION_CHANNEL = "notification:message";

    private final RedisMessageListenerContainer container;
    private final SseEmitterRepository sseEmitterRepository;
    private final ObjectMapper objectMapper;
    private final NotificationCommandService notificationCommandService;

    @PostConstruct
    public void init() {
        container.addMessageListener(this, new ChannelTopic(NOTIFICATION_CHANNEL));
        log.info("Redis 구독 시작 - channel: {}", NOTIFICATION_CHANNEL);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody(), java.nio.charset.StandardCharsets.UTF_8);
            NotificationEvent event = objectMapper.readValue(body, NotificationEvent.class);

            log.info("Redis 알림 수신 - userId: {}, notificationId: {}",
                    event.userId(), event.notification().id());

            notificationCommandService.sendNotification(
                    event.userId(),
                    event.notification());

        } catch (Exception e) {
            log.error("Redis 알림 처리 실패", e);
        }
    }

    private void sendToUser(Long userId, Object notification) {
        SseEmitter emitter = sseEmitterRepository.get(userId);
        if (emitter == null) {
            log.debug("SSE 연결 없음 - userId: {}", userId);
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("notification")
                    .data(notification));
            log.info("SSE 알림 전송 성공 - userId: {}", userId);
        } catch (IOException e) {
            log.error("SSE 알림 전송 실패 - userId: {}", userId, e);
            sseEmitterRepository.deleteById(userId);
        }
    }
}
