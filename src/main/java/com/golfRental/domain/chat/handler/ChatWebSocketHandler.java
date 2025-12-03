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

        log.info("WebSocket 연결 - chatRoomId: {}, userId: {}, sessionId: {}",
                chatRoomId, userId, session.getId());

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

        log.info("WebSocket 메시지 수신 - chatRoomId: {}", chatRoomId);

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

        chatRoomSessions.computeIfPresent(chatRoomId, (key, currentSessions) -> {
            currentSessions.remove(session.getId());
            int remainingCount = currentSessions.size();
            log.info("WebSocket 연결 종료 완료 - chatRoomId: {}, 남은 접속자 수: {}",
                    chatRoomId, remainingCount);
            return currentSessions.isEmpty() ? null : currentSessions;
        });
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
        String path = session.getUri().getPath();
        if (path == null) {
            throw new IllegalStateException("URI path is null");
        }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            throw new IllegalStateException("Cannot extract chatRoomId from path: " + path);
        }

        String chatRoomIdStr = path.substring(lastSlashIndex + 1);
        try {
            return Long.parseLong(chatRoomIdStr);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid chatRoomId in path: " + chatRoomIdStr, e);
        }
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        Object userIdAttr = session.getAttributes().get("userId");
        if (userIdAttr == null) {
            throw new IllegalStateException("userId not found in session attributes");
        }
        if (!(userIdAttr instanceof Long)) {
            throw new IllegalStateException("userId attribute is not of type Long: " + userIdAttr.getClass().getName());
        }
        return (Long) userIdAttr;
    }
}