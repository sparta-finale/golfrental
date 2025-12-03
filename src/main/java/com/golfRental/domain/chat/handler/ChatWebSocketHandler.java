package com.golfRental.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golfRental.domain.chat.dto.request.ChatMessageCreateRequest;
import com.golfRental.domain.chat.dto.response.ChatMessageResponse;
import com.golfRental.domain.chat.entity.ChatRoom;
import com.golfRental.domain.chat.service.command.ChatCommandService;
import com.golfRental.domain.chat.service.query.ChatQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;
    private final ObjectMapper objectMapper;

    private final Map<Long, Map<String, WebSocketSession>> chatRoomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long chatRoomId = getChatRoomId(session);
        Long userId = getUserIdFromSession(session);

        ChatRoom chatRoom = chatQueryService.findById(chatRoomId);
        if (!chatRoom.isParticipant(userId)) {
            log.warn("WebSocket 권한 없음 - chatRoomId: {}, userId: {}", chatRoomId, userId);
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        chatRoomSessions.computeIfAbsent(chatRoomId, k -> new ConcurrentHashMap<>())
                .put(session.getId(), session);

        log.info("WebSocket 연결 완료 - chatRoomId: {}, 현재 접속자 수: {}",
                chatRoomId, chatRoomSessions.get(chatRoomId).size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long chatRoomId = getChatRoomId(session);
        Long userId = getUserIdFromSession(session);

        try {
            ChatMessageCreateRequest request = objectMapper.readValue(
                    message.getPayload(), ChatMessageCreateRequest.class);

            ChatMessageResponse savedMessage = chatCommandService.sendMessage(
                    userId, chatRoomId, request);

            String responseJson = objectMapper.writeValueAsString(savedMessage);
            broadcastMessage(chatRoomId, responseJson);

        } catch (Exception e) {
            log.error("WebSocket 메시지 처리 실패 - chatRoomId: {}, userId: {}",
                    chatRoomId, userId, e);
            sendErrorMessage(session, "메시지 전송에 실패했습니다.");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long chatRoomId = getChatRoomId(session);

        log.info("WebSocket 연결 종료 - chatRoomId: {}, sessionId: {}, status: {}",
                chatRoomId, session.getId(), status);

        Map<String, WebSocketSession> sessions = chatRoomSessions.get(chatRoomId);
        if (sessions != null) {
            sessions.remove(session.getId());
            if (sessions.isEmpty()) {
                chatRoomSessions.remove(chatRoomId);
            }
        }

        log.info("WebSocket 연결 종료 완료 - chatRoomId: {}, 남은 접속자 수: {}",
                chatRoomId, sessions != null ? sessions.size() : 0);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket 에러 - sessionId: {}", session.getId(), exception);
        session.close(CloseStatus.SERVER_ERROR);
    }

    private void broadcastMessage(Long chatRoomId, String message) {
        Map<String, WebSocketSession> sessions = chatRoomSessions.get(chatRoomId);
        if (sessions == null) {
            return;
        }

        sessions.values().forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                log.error("메시지 전송 실패 - sessionId: {}", session.getId(), e);
            }
        });
    }

    private void sendErrorMessage(WebSocketSession session, String errorMessage) {
        try {
            if (session.isOpen()) {
                Map<String, String> error = Map.of("error", errorMessage);
                String errorJson = objectMapper.writeValueAsString(error);
                session.sendMessage(new TextMessage(errorJson));
            }
        } catch (IOException e) {
            log.error("에러 메시지 전송 실패 - sessionId: {}", session.getId(), e);
        }
    }

    private Long getChatRoomId(WebSocketSession session) {
        String uri = session.getUri().toString();
        String[] parts = uri.split("/");
        String lastPart = parts[parts.length - 1];

        if (lastPart.contains("?")) {
            lastPart = lastPart.substring(0, lastPart.indexOf("?"));
        }

        return Long.parseLong(lastPart);
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        Object userId = session.getAttributes().get("userId");
        if (userId == null) {
            throw new IllegalStateException("userId not found in session attributes");
        }
        return (Long) userId;
    }

}
