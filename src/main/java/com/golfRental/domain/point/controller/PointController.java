package com.golfRental.domain.point.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.point.dto.request.PointUseRequest;
import com.golfRental.domain.point.dto.response.PointBalanceResponse;
import com.golfRental.domain.point.dto.response.PointTransactionGetResponse;
import com.golfRental.domain.point.dto.response.PointUseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

public interface PointController {

    @Operation(
            summary = "내 포인트 잔액 조회",
            description = "로그인한 사용자의 포인트 잔액을 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PointBalanceResponse>> getMyPointBalance(
            AuthUser authUser
    );

    @Operation(
            summary = "내 포인트 거래 내역 조회",
            description = "로그인한 사용자의 포인트 거래 기록을 페이징 형태로 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<PointTransactionGetResponse>>> getMyTransactions(
            AuthUser authUser,
            int page,
            int size
    );

    @Operation(
            summary = "포인트 사용",
            description = "로그인한 사용자가 포인트를 사용합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "포인트 사용 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 잔액 부족"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PointUseResponse>> usePoints(
            AuthUser authUser,
            PointUseRequest request
    );
}
