package com.golfRental.domain.auth.service.command;

import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;
import com.golfRental.domain.auth.exception.AuthErrorCode;
import com.golfRental.domain.auth.exception.AuthException;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.command.UserCommandService;
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
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void signup(AuthSignupRequest authSignupRequest) {
        if (userQueryService.existsByEmail(authSignupRequest.email())) {
            log.warn("회원가입 실패 - 이메일 중복 - email: {}", authSignupRequest.email());
            throw new AuthException(AuthErrorCode.AUTH_DUPLICATE_EMAIL);
        }
        if (userQueryService.existsByNickname(authSignupRequest.nickname())) {
            log.warn("회원가입 실패 - 닉네임 중복 - nickname: {}", authSignupRequest.nickname());
            throw new AuthException(AuthErrorCode.AUTH_DUPLICATE_NICKNAME);
        }
        if (userQueryService.existsByPhoneNumber(authSignupRequest.phoneNumber())) {
            log.warn("회원가입 실패 - 전화번호 중복 - phoneNumber: {}", authSignupRequest.phoneNumber());
            throw new AuthException(AuthErrorCode.AUTH_DUPLICATE_PHONE_NUMBER);
        }

        String encodedPassword = passwordEncoder.encode(authSignupRequest.password());

        User user = User.create(
                authSignupRequest.email(),
                encodedPassword,
                authSignupRequest.username(),
                authSignupRequest.phoneNumber(),
                authSignupRequest.address(),
                authSignupRequest.nickname()
        );

        userCommandService.save(user);
    }

    @Override
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {

        User user = userQueryService.findByEmail(authLoginRequest.email());

        if (!passwordEncoder.matches(authLoginRequest.password(), user.getPassword())) {
            log.warn("로그인 실패 - 잘못된 비밀번호 - email: {}", authLoginRequest.email());
            throw new AuthException(AuthErrorCode.AUTH_INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.createToken(user.getId(), user.getRole());

        return AuthLoginResponse.create(accessToken);
    }

    @Override
    public void logout(Long userId) {
        // access token 삭제는 클라이언트 측에서 진행해야 함
        // 추후 refresh 토큰이 들어오게 된다면 그 때 작업
    }
}
