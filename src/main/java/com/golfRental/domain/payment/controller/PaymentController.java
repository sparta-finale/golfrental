package com.golfRental.domain.payment.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.payment.dto.request.PaymentConfirmRequest;
import com.golfRental.domain.payment.dto.response.PaymentConfirmResponse;
import com.golfRental.domain.payment.dto.response.PaymentHistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;

@Tag(name = "결제 관리", description = "결제 관련 API")
public interface PaymentController {

    @Operation(
            summary = "결제 승인",
            description = "사용자가 토스 결제를 승인합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 승인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "결제 내역을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<PaymentConfirmResponse>> confirmPayment(
            PaymentConfirmRequest request,
            AuthUser authUser
    );

    @Operation(
            summary = "결제 내역 조회",
            description = "사용자의 결제 내역을 페이지네이션하여 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    ResponseEntity<CommonApiResponse<Slice<PaymentHistoryResponse>>> getMyPayments(
            AuthUser authUser,
            int page,
            int size
    );
}
