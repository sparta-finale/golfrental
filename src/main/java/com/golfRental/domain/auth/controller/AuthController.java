package com.golfRental.domain.auth.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.auth.dto.request.AuthLoginRequest;
import com.golfRental.domain.auth.dto.request.AuthSignupRequest;
import com.golfRental.domain.auth.dto.response.AuthLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 관리", description = "회원 관련 API")
public interface AuthController {

    @Operation(
            summary = "회원 생성",
            description = "새로운 회원을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "409", description = "이메일 중복 실패"),
                    @ApiResponse(responseCode = "409", description = "닉네임 중복 실패"),
                    @ApiResponse(responseCode = "409", description = "전화번호 중복 실패")
            }
    )
    ResponseEntity<CommonApiResponse<Void>> signup(
            AuthSignupRequest authSignupRequest
    );

    @Operation(
            summary = "회원 로그인",
            description = "아이디와 비밀번호를 사용하여 로그인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "비밀번호 불일치 실패")
            }
    )
    ResponseEntity<CommonApiResponse<AuthLoginResponse>> login(
            AuthLoginRequest authLoginRequest
    );

    @Operation(
            summary = "로그아웃",
            description = "로그아웃 됩니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            }
    )
    ResponseEntity<CommonApiResponse<Void>> logout(
            AuthUser authUser
    );
}
