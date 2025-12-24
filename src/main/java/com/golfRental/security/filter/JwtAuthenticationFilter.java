package com.golfRental.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.user.enums.UserRole;
import com.golfRental.security.authentication.JwtAuthenticationToken;
import com.golfRental.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "bearer ";

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    /**
     * 인증이 필요 없는 경로는 JWT 필터 자체를 타지 않음
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // CORS Preflight 요청은 무조건 제외
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        return
                // Auth
                path.startsWith("/api/v1/login") ||
                        path.startsWith("/api/v1/signup") ||

                        // Public API
                        path.startsWith("/api/v1/public") ||

                        // Swagger / Actuator
                        path.startsWith("/v3/api-docs") ||
                        path.startsWith("/swagger-ui") ||
                        path.startsWith("/actuator") ||

                        // Static / Front entry
                        path.equals("/") ||
                        path.startsWith("/favicon") ||
                        path.startsWith("/static") ||
                        path.startsWith("/assets") ||
                        path.endsWith(".html");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {

        // JWT 추출 로직 통합
        String jwt = resolveToken(request);

        // 토큰이 없으면 인증 시도 없이 다음 필터
        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }

        // JWT 검증 및 인증 설정
        if (!processAuthentication(jwt, request, response)) {
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * JWT 토큰 추출
     * 1. Authorization Header (Bearer, case-insensitive)
     * 2. access_token Query Parameter (SSE 대응)
     */
    private String resolveToken(HttpServletRequest request) {

        // 1. Authorization Header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null &&
                authorizationHeader.toLowerCase().startsWith(BEARER_PREFIX)) {

            return authorizationHeader.substring(BEARER_PREFIX.length());
        }

        // 2. SSE Query Parameter
        String accessToken = request.getParameter("access_token");
        if (accessToken != null && !accessToken.isBlank()) {
            if (accessToken.toLowerCase().startsWith(BEARER_PREFIX)) {
                return accessToken.substring(BEARER_PREFIX.length());
            }
            return accessToken;
        }

        return null;
    }

    // JWT 토큰을 검증하고 SecurityContext에 인증 정보를 설정
    private boolean processAuthentication(
            String jwt,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        try {
            // JWT 토큰을 파싱하여 Claims(토큰에 담긴 정보) 추출
            Claims claims = jwtUtil.extractClaims(jwt);

            // SecurityContext에 인증 정보가 없으면 설정 (이미 인증된 경우 중복 설정 방지)
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                setAuthentication(claims);
            }
            return true; // 검증 성공

        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");
            return false;

        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.");
            return false;

        } catch (Exception e) {
            log.error("JWT 처리 중 서버 오류", e);
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

            return false;
        }
    }

    // JWT Claims에서 사용자 정보를 추출하여 Spring Security의 인증 정보 설정
    private void setAuthentication(Claims claims) {
        // JWT의 subject claim에서 사용자 ID 추출 (subject는 JWT 표준 claim)
        Long userId = Long.valueOf(claims.getSubject());
        // 커스텀 claim에서 사용자 권한 정보를 추출하여 enum으로 변환
        UserRole userRole = UserRole.of(claims.get("userRole", String.class));

        AuthUser authUser = new AuthUser(userId, userRole);
        Authentication authentication = new JwtAuthenticationToken(authUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void sendErrorResponse(
            HttpServletResponse response,
            HttpStatus status,
            String message
    ) throws IOException {

        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new HashMap<>();
        body.put("status", status.name());
        body.put("code", status.value());
        body.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
