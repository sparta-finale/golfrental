package com.golfRental.domain.chat.publisher;


import com.golfRental.domain.chat.dto.event.ChatMessageEvent;
import com.golfRental.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRedisPublisher {

    private static final String CHAT_CHANNEL = "chat:message";
    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public void publish(ChatMessageEvent event) {
        try {
            redisTemplate.convertAndSend(CHAT_CHANNEL, event);
            log.info("Redis 메시지 발행 성공 - chatRoomId: {}, messageId: {}",
                    event.getChatRoomId(), event.getMessage().messageId());
        } catch (Exception e) {
            log.error("Redis 메시지 발행 실패 - chatRoomId: {}", event.getChatRoomId(), e);
            throw new RuntimeException("Failed to publish message to Redis", e);
        }
    }
}
