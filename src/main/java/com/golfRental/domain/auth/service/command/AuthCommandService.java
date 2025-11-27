package com.golfRental.domain.auth.service.command;

import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;

public interface AuthCommandService {

    // 회원가입
    void signup(AuthSignupRequest authSignupRequest);

    // 로그인
    AuthLoginResponse login(AuthLoginRequest authLoginRequest);
}
