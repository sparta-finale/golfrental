package com.golfRental.domain.user.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.user.dto.response.UserGetMyInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface UserController {

    /**
     * 내 정보 조회
     *
     * @param authUser
     * @return UserGetMyInfoResponse
     */
    ResponseEntity<CommonApiResponse<UserGetMyInfoResponse>> getMyInfo(
            @AuthenticationPrincipal AuthUser authUser
    );
}
