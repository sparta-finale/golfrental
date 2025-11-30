package com.golfRental.domain.user.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.PageResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.user.dto.request.UserUpdateMyInfoRequest;
import com.golfRental.domain.user.dto.request.UserUpdatePasswordRequest;
import com.golfRental.domain.user.dto.response.UserGetAllResponse;
import com.golfRental.domain.user.dto.response.UserGetInfoResponse;
import com.golfRental.domain.user.dto.response.UserGetMyInfoResponse;
import com.golfRental.domain.user.dto.response.UserUpdateMyInfoResponse;
import com.golfRental.domain.user.message.UserSuccessMessage;
import com.golfRental.domain.user.service.command.UserCommandService;
import com.golfRental.domain.user.service.query.UserQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/users/{userId}")
    public ResponseEntity<CommonApiResponse<UserGetInfoResponse>> getInfo(
            @PathVariable Long userId
    ) {
        UserGetInfoResponse userGetInfoResponse = userQueryService.getInfo(userId);

        return CommonApiResponse.success(userGetInfoResponse, UserSuccessMessage.GET_INFO);
    }

    @Override
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonApiResponse<PageResponse<UserGetAllResponse>>> getAll(
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageResponse<UserGetAllResponse> users = userQueryService.getAll(pageable);

        return CommonApiResponse.pageSuccess(users, UserSuccessMessage.GET_USER_INFO);
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

    @Override
    @PatchMapping("/users/me/password")
    public ResponseEntity<CommonApiResponse<Void>> changePassword(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest
    ) {
        userCommandService.updatePassword(authUser.getUserId(), userUpdatePasswordRequest);

        return CommonApiResponse.success(null, UserSuccessMessage.UPDATE_MY_PASSWORD);
    }
}
