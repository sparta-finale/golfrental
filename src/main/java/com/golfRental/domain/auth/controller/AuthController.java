package com.golfRental.domain.auth.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    /**
     * 회원가입
     *
     * @param authSignupRequest 회원가입 시 필요 데이터
     * @return void
     */
    ResponseEntity<CommonApiResponse<Void>> signup(
            AuthSignupRequest authSignupRequest
    );

    /**
     * 로그인
     *
     * @param authLoginRequest 로그인 시 필요 데이터
     * @return AuthLoginResponse
     */
    ResponseEntity<CommonApiResponse<AuthLoginResponse>> login(
            AuthLoginRequest authLoginRequest
    );

    /**
     * 로그아웃
     *
     * @param authUser 토큰 정보
     * @return void
     */
    ResponseEntity<CommonApiResponse<Void>> logout(
            AuthUser authUser
    );
}
