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
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserController {

    /**
     * 내 정보 조회 API
     *
     * @param authUser 유저 토큰 정보
     * @return UserGetMyInfoResponse
     */
    ResponseEntity<CommonApiResponse<UserGetMyInfoResponse>> getMyInfo(
            AuthUser authUser
    );

    /**
     * 유저 정보 단건 조회 API
     *
     * @param userId 조회할 유저 ID
     * @return UserGetInfoResponse
     */
    ResponseEntity<CommonApiResponse<UserGetInfoResponse>> getInfo(
            Long userId
    );

    /**
     * 유저 조회(ADMIN) API
     *
     * @return PageResponse<UserGetAllResponse>
     */
    ResponseEntity<CommonApiResponse<PageResponse<UserGetAllResponse>>> getAll(
            Pageable pageable
    );

    /**
     * 내 정보 수정 API
     *
     * @param authUser                유저 토큰 정보
     * @param userUpdateMyInfoRequest 내 정보 수정사항
     * @return UserUpdateMyInfoResponse
     */
    ResponseEntity<CommonApiResponse<UserUpdateMyInfoResponse>> updateMyInfo(
            AuthUser authUser,
            UserUpdateMyInfoRequest userUpdateMyInfoRequest
    );

    /**
     * 유저 비밀번호 수정 API
     *
     * @param authUser                  유저 토큰 정보
     * @param userUpdatePasswordRequest 비밀번호 수정에 필요한 데이터
     * @return void
     */
    ResponseEntity<CommonApiResponse<Void>> changePassword(
            AuthUser authUser,
            UserUpdatePasswordRequest userUpdatePasswordRequest
    );
}
