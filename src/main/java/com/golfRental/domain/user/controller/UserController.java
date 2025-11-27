package com.golfRental.domain.user.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.user.dto.request.UserUpdateMyInfoRequest;
import com.golfRental.domain.user.dto.response.UserGetMyInfoResponse;
import com.golfRental.domain.user.dto.response.UserUpdateMyInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {

    /**
     * 내 정보 조회
     *
     * @param authUser 유저 토큰 정보
     * @return UserGetMyInfoResponse
     */
    ResponseEntity<CommonApiResponse<UserGetMyInfoResponse>> getMyInfo(
            @AuthenticationPrincipal AuthUser authUser
    );

    /**
     * 내 정보 수정
     *
     * @param authUser                유저 토큰 정보
     * @param userUpdateMyInfoRequest 내 정보 수정사항
     * @return UserUpdateMyInfoResponse
     */
    ResponseEntity<CommonApiResponse<UserUpdateMyInfoResponse>> updateMyInfo(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserUpdateMyInfoRequest userUpdateMyInfoRequest
    );
}
