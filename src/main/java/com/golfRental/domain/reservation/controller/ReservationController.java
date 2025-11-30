package com.golfRental.domain.reservation.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;
import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import org.springframework.http.ResponseEntity;

public interface ReservationController {

    /**
     * 예약 생성
     *
     * @param request  예약 생성 요청 DTO
     * @param authUser 로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationCreateResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationCreateResponse>> createReservation(
            ReservationCreateRequest request,
            AuthUser authUser
    );

    /**
     * 예약 단건 조회
     *
     * @param reservationId 조회할 예약 ID
     * @param authUser      로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationGetResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationGetResponse>> getReservation(
            Long reservationId,
            AuthUser authUser
    );
}
