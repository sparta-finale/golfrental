package com.golfRental.domain.chat.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golfRental.domain.chat.dto.event.ChatMessageEvent;
import com.golfRental.domain.chat.handler.ChatWebSocketHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRedisSubscriber implements MessageListener {

    private static final String CHAT_CHANNEL = "chat:message";

    private final RedisMessageListenerContainer container;
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        container.addMessageListener(this, new ChannelTopic(CHAT_CHANNEL));
        log.info("Redis 구독 시작 - channel: {}", CHAT_CHANNEL);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            ChatMessageEvent event = objectMapper.readValue(body, ChatMessageEvent.class);

            log.info("Redis 메시지 수신 - chatRoomId: {}, messageId: {}",
                    event.chatRoomId(), event.message().messageId());

            chatWebSocketHandler.broadcastToRoom(event.chatRoomId(), event.message());

        } catch (Exception e) {
            log.error("Redis 메시지 처리 실패", e);
        }
    }
}
