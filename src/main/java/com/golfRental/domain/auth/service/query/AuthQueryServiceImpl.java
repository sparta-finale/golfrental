package com.golfRental.domain.auth.service.query;

import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;
import com.golfRental.domain.auth.exception.AuthErrorCode;
import com.golfRental.domain.auth.exception.AuthException;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import com.golfRental.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthQueryServiceImpl implements AuthQueryService {

    private final UserQueryService userQueryService;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {

        User user = userQueryService.findByEmail(authLoginRequest.getEmail());

        if (!passwordEncoder.matches(authLoginRequest.getPassword(), user.getPassword())) {
            log.warn("로그인 실패 - 잘못된 비밀번호 - email: {}", authLoginRequest.getEmail());
            throw new AuthException(AuthErrorCode.AUTH_INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.createToken(user.getId(), user.getRole());

        AuthLoginResponse authLoginResponse = AuthLoginResponse.builder().accessToken(accessToken).build();

        return authLoginResponse;
    }
}
