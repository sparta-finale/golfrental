package com.golfRental.domain.auth.service.command;

import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;

public interface AuthCommandService {

    /**
     * 회원가입
     *
     * @param authSignupRequest 회원가입 시 필요 데이터
     */
    void signup(AuthSignupRequest authSignupRequest);

    /**
     * 로그인
     *
     * @param authLoginRequest 로그인 시 필요 데이터
     * @return AuthLoginResponse
     */
    AuthLoginResponse login(AuthLoginRequest authLoginRequest);

    /**
     * 로그아웃
     *
     * @param userId 유저 ID
     */
    void logout(Long userId);
}
