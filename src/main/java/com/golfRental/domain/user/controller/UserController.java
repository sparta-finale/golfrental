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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 관리", description = "회원 관련 API")
public interface UserController {

    @Operation(
            summary = "내 정보 조회",
            description = "자신의 상세 정보를 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "내 정보 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "해당 ID 불일치 실패")
            }
    )
    ResponseEntity<CommonApiResponse<UserGetMyInfoResponse>> getMyInfo(
            AuthUser authUser
    );

    @Operation(
            summary = "유저 정보 상세 조회",
            description = "유저 상세 정보를 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "유저 정보 상세 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "해당 ID 불일치 실패")
            }
    )
    ResponseEntity<CommonApiResponse<UserGetInfoResponse>> getInfo(
            Long userId
    );

    @Operation(
            summary = "모든 유저 정보 조회(ADMIN)",
            description = "모든 유저 정보를 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "모든 유저 정보 조회 성공")
            }
    )
    ResponseEntity<CommonApiResponse<PageResponse<UserGetAllResponse>>> getAll(
            Pageable pageable
    );

    @Operation(
            summary = "내 정보 수정",
            description = "내 정보를 수정합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "내 정보 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청:\n- 이메일 중복\n- 닉네임 중복\n- 전화번호 중복")
            }
    )
    ResponseEntity<CommonApiResponse<UserUpdateMyInfoResponse>> updateMyInfo(
            AuthUser authUser,
            UserUpdateMyInfoRequest userUpdateMyInfoRequest
    );

    @Operation(
            summary = "내 비밀번호 수정",
            description = "내 비밀번호를 수정합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "내 비밀번호 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청:\n- 현재 비밀번호 불일치\n- 현재 비밀번호와 수정하려는 비밀번호 동일")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> changePassword(
            AuthUser authUser,
            UserUpdatePasswordRequest userUpdatePasswordRequest
    );
}
