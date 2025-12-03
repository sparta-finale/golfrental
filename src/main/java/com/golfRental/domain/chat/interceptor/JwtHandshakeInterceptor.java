package com.golfRental.domain.chat.interceptor;

import com.golfRental.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        String query = request.getURI().getQuery();

        if (query == null || !query.contains("token=")) {
            log.warn("WebSocket 연결 실패 - JWT 토큰 없음");
            return false;
        }

        String token = extractToken(query);

        try {
            if (!jwtUtil.validateToken(token)) {
                log.warn("WebSocket 연결 실패 - JWT 토큰 유효하지 않음");
                return false;
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            attributes.put("userId", userId);
            
            return super.beforeHandshake(request, response, wsHandler, attributes);

        } catch (Exception e) {
            log.error("WebSocket JWT 검증 실패", e);
            return false;
        }
    }

    private String extractToken(String query) {
        String[] params = query.split("&");
        for (String param : params) {
            if (param.startsWith("token=")) {
                return param.substring(6);
            }
        }
        return null;
    }
}