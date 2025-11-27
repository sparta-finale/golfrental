package com.golfRental.domain.auth.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    /**
     * 회원가입
     *
     * @param authSignupRequest
     * @return void
     */
    ResponseEntity<CommonApiResponse<Void>> signup(
            @Valid @RequestBody AuthSignupRequest authSignupRequest
    );

    /**
     * 로그인
     *
     * @param authLoginRequest
     * @return 토큰
     */
    ResponseEntity<CommonApiResponse<AuthLoginResponse>> login(
            @Valid @RequestBody AuthLoginRequest authLoginRequest
    );
}
