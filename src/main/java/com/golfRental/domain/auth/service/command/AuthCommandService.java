package com.golfRental.domain.auth.service.command;

import com.golfRental.domain.auth.dto.request.AuthSignupRequest;

public interface AuthCommandService {

    // 회원가입
    void signup(AuthSignupRequest authSignupRequest);
}
