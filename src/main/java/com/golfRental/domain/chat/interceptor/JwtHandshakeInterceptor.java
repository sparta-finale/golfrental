package com.golfRental.domain.chat.interceptor;

import com.golfRental.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        String token = extractTokenFromCookie(request);

        if (token == null) {
            log.warn("WebSocket 연결 실패 - JWT 토큰 없음");
            return false;
        }

        try {
            if (!jwtUtil.validateToken(token)) {
                log.warn("WebSocket 연결 실패 - JWT 토큰 유효하지 않음");
                return false;
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            attributes.put("userId", userId);

            log.info("WebSocket JWT 인증 성공 - userId: {}", userId);
            return true;

        } catch (Exception e) {
            log.error("WebSocket JWT 검증 실패", e);
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }

    private String extractTokenFromCookie(ServerHttpRequest request) {
        List<String> cookies = request.getHeaders().get(HttpHeaders.COOKIE);
        if (cookies == null || cookies.isEmpty()) {
            return null;
        }

        for (String cookie : cookies) {
            String[] pairs = cookie.split(";");
            for (String pair : pairs) {
                String[] keyValue = pair.trim().split("=", 2);
                if (keyValue.length == 2 && "Authorization".equals(keyValue[0])) {
                    try {
                        String token = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                        if (token.startsWith(BEARER_PREFIX)) {
                            return token.substring(BEARER_PREFIX.length());
                        }
                        return token;
                    } catch (java.io.UnsupportedEncodingException e) {
                        log.warn("Failed to decode Authorization cookie value", e);
                        return null;
                    }
                }
            }
        }
        return null;
    }
}