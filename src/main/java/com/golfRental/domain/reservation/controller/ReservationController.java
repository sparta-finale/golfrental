package com.golfRental.domain.reservation.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;
import com.golfRental.domain.reservation.dto.response.ReservationGetAllResponse;
import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import com.golfRental.domain.reservation.dto.response.ReservationUpdateStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "예약 관리", description = "예약 관련 API")
public interface ReservationController {

    @Operation(
            summary = "예약 생성",
            description = "사용자가 게시글에 대해 예약을 생성합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "409", description = "예약 가능 기간 겹침")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationCreateResponse>> createReservation(
            ReservationCreateRequest request,
            AuthUser authUser
    );

    @Operation(
            summary = "예약 단건 조회",
            description = "예약 ID로 예약 상세 정보를 조회합니다. 예약 당사자(호스트 또는 게스트)만 조회할 수 있습니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "예약 상세 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "예약 당사자만 조회 가능"),
                    @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationGetResponse>> getReservation(
            Long reservationId,
            AuthUser authUser
    );

    @Operation(
            summary = "내 예약 목록 조회",
            description = "사용자가 자신의 예약 목록을 페이징 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "예약 목록 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    ResponseEntity<CommonApiResponse<SliceResponse<ReservationGetAllResponse>>> getMyReservations(
            AuthUser authUser,
            int page,
            int size
    );

    @Operation(
            summary = "예약 승인",
            description = "호스트가 예약 요청을 승인합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "예약 승인 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "호스트만 승인 가능"),
                    @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> approveReservation(
            Long reservationId,
            AuthUser authUser
    );

    @Operation(
            summary = "예약 거절",
            description = "호스트가 예약 요청을 거절합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "예약 거절 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "호스트만 거절 가능"),
                    @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> rejectReservation(
            Long reservationId,
            AuthUser authUser
    );

    @Operation(
            summary = "예약 취소",
            description = "게스트가 자신의 예약을 취소합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "예약 취소 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "게스트만 취소 가능"),
                    @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> cancelReservation(
            Long reservationId,
            AuthUser authUser
    );

    @Operation(
            summary = "대여 시작",
            description = "호스트가 예약된 대여를 시작 처리합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "대여 시작 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "호스트만 대여 시작 가능"),
                    @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> startReservation(
            Long reservationId,
            AuthUser authUser
    );

    @Operation(
            summary = "반납 요청",
            description = "게스트가 반납을 요청합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "반납 요청 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "게스트만 반납 요청 가능"),
                    @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> requestReturn(
            Long reservationId,
            AuthUser authUser
    );

    @Operation(
            summary = "반납 완료",
            description = "호스트가 반납을 승인하여 예약을 완료 처리합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "반납 완료 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "호스트만 반납 완료 가능"),
                    @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> completeReservation(
            Long reservationId,
            AuthUser authUser
    );

    @Operation(
            summary = "예약 상태 조회",
            description = "호스트 또는 게스트가 예약 상태를 조회합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "예약 상태 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "예약 당사자만 조회 가능"),
                    @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> getReservationStatus(
            Long reservationId,
            AuthUser authUser
    );
}