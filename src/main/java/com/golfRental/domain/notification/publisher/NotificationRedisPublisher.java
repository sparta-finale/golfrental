package com.golfRental.domain.notification.publisher;

import com.golfRental.domain.notification.dto.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRedisPublisher {

    private static final String NOTIFICATION_CHANNEL = "notification:message";
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(NotificationEvent event) {
        try {
            redisTemplate.convertAndSend(NOTIFICATION_CHANNEL, event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish notification to Redis", e);
        }
    }
}
