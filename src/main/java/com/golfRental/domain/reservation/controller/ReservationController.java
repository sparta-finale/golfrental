package com.golfRental.domain.reservation.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import org.springframework.http.ResponseEntity;

public interface ReservationController {

    /**
     * 예약 단건 조회
     *
     * @param reservationId 조회할 예약 ID
     * @param authUser 로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationGetResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationGetResponse>> getReservation(
            Long reservationId,
            AuthUser authUser
    );
}