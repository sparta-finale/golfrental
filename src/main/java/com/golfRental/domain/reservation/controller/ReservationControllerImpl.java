package com.golfRental.domain.reservation.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import com.golfRental.domain.reservation.message.ReservationSuccessMessage;
import com.golfRental.domain.reservation.service.query.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationControllerImpl implements ReservationController {

    private final ReservationQueryService reservationQueryService;

    @Override
    @GetMapping("/{reservationId}")
    public ResponseEntity<CommonApiResponse<ReservationGetResponse>> getReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ReservationGetResponse reservationGetResponse =
                reservationQueryService.findById(reservationId, authUser.getUserId());

        return CommonApiResponse.success(
                reservationGetResponse,
                ReservationSuccessMessage.GET_RESERVATION
        );
    }
}
