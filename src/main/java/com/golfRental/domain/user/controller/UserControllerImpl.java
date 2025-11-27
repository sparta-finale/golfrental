package com.golfRental.domain.user.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.user.dto.request.UserUpdateMyInfoRequest;
import com.golfRental.domain.user.dto.response.UserGetMyInfoResponse;
import com.golfRental.domain.user.dto.response.UserUpdateMyInfoResponse;
import com.golfRental.domain.user.message.UserSuccessMessage;
import com.golfRental.domain.user.service.command.UserCommandService;
import com.golfRental.domain.user.service.query.UserQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserControllerImpl implements UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Override
    @GetMapping("/users/me")
    public ResponseEntity<CommonApiResponse<UserGetMyInfoResponse>> getMyInfo(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        UserGetMyInfoResponse userGetMyInfoResponse = userQueryService.getMyInfo(authUser.getUserId());

        return CommonApiResponse.success(userGetMyInfoResponse, UserSuccessMessage.GET_MY_INFO);
    }

    @Override
    @PutMapping("/users/me")
    public ResponseEntity<CommonApiResponse<UserUpdateMyInfoResponse>> updateMyInfo(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UserUpdateMyInfoRequest userUpdateMyInfoRequest
    ) {
        UserUpdateMyInfoResponse userUpdateMyInfoResponse = userCommandService.updateMyInfo(
                authUser.getUserId(), userUpdateMyInfoRequest
        );

        return CommonApiResponse.success(userUpdateMyInfoResponse, UserSuccessMessage.UPDATE_MY_INFO);
    }
}
