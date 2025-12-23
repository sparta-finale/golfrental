package com.golfRental.common.config;

import com.golfRental.domain.chat.handler.ChatWebSocketHandler;
import com.golfRental.domain.chat.interceptor.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Arrays;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    /**
     * cors.allowed-origins가 없는 환경(local/test)을 대비해
     * default 값을 빈 문자열로 설정
     */
    @Value("${cors.allowed-origins:}")
    private String[] allowedOrigins;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        String[] origins = Arrays.stream(allowedOrigins)
                .filter(StringUtils::hasText)
                .toArray(String[]::new);

        var handler = registry.addHandler(
                        chatWebSocketHandler,
                        "/ws/chat/{chatRoomId}"
                )
                .addInterceptors(jwtHandshakeInterceptor);

        // origin 설정이 있을 때만 CORS 적용
        if (origins.length > 0) {
            handler.setAllowedOrigins(origins);
        }
        // 없으면 CORS 미설정 (local / test 환경 대응)
    }
}
