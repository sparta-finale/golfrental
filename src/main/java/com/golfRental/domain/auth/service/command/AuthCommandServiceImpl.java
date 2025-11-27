package com.golfRental.domain.auth.service.command;

import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import com.golfRental.domain.auth.exception.AuthErrorCode;
import com.golfRental.domain.auth.exception.AuthException;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.command.UserCommandService;
import com.golfRental.domain.user.service.query.UserQueryService;
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

    @Override
    public void signup(AuthSignupRequest authSignupRequest) {
        if (userQueryService.existsByEmail(authSignupRequest.getEmail())) {
            log.warn("회원가입 실패 - 이메일 중복 - email: {}", authSignupRequest.getEmail());
            throw new AuthException(AuthErrorCode.AUTH_DUPLICATE_EMAIL);
        }
        if (userQueryService.existsByNickname(authSignupRequest.getNickname())) {
            log.warn("회원가입 실패 - 닉네임 중복 - nickname: {}", authSignupRequest.getNickname());
            throw new AuthException(AuthErrorCode.AUTH_DUPLICATE_NICKNAME);
        }
        if (userQueryService.existsByPhoneNumber(authSignupRequest.getPhoneNumber())) {
            log.warn("회원가입 실패 - 전화번호 중복 - phoneNumber: {}", authSignupRequest.getPhoneNumber());
            throw new AuthException(AuthErrorCode.AUTH_DUPLICATE_PHONE_NUMBER);
        }

        String encodedPassword = passwordEncoder.encode(authSignupRequest.getPassword());

        User user = User.builder()
                .email(authSignupRequest.getEmail())
                .password(encodedPassword)
                .name(authSignupRequest.getUsername())
                .phoneNumber(authSignupRequest.getPhoneNumber())
                .address(authSignupRequest.getAddress())
                .nickname(authSignupRequest.getNickname())
                .build();

        userCommandService.save(user);
    }
}
