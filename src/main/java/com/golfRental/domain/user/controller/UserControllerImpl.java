package com.golfRental.domain.user.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.user.dto.response.UserGetMyInfoResponse;
import com.golfRental.domain.user.message.UserSuccessMessage;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserControllerImpl implements UserController {

    private final UserQueryService userQueryService;

    @Override
    @GetMapping("/users/me")
    public ResponseEntity<CommonApiResponse<UserGetMyInfoResponse>> getMyInfo(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        UserGetMyInfoResponse userGetMyInfoResponse = userQueryService.getMyInfo(authUser.getUserId());

        return CommonApiResponse.success(userGetMyInfoResponse, UserSuccessMessage.GET_MY_INFO);
    }
}
