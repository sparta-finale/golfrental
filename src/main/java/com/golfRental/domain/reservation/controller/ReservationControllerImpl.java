package com.golfRental.domain.reservation.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;
import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import com.golfRental.domain.reservation.message.ReservationSuccessMessage;
import com.golfRental.domain.reservation.service.command.ReservationCommandService;
import com.golfRental.domain.reservation.service.query.ReservationQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationControllerImpl implements ReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    @Override
    @PostMapping
    public ResponseEntity<CommonApiResponse<ReservationCreateResponse>> createReservation(
            @Valid @RequestBody ReservationCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        ReservationCreateResponse response =
                reservationCommandService.createReservation(request, authUser.getUserId());

        return CommonApiResponse.created(
                response,
                ReservationSuccessMessage.RESERVATION_CREATED
        );
    }

    @Override
    @GetMapping("/{reservationId}")
    public ResponseEntity<CommonApiResponse<ReservationGetResponse>> getReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        ReservationGetResponse response =
                reservationQueryService.findById(reservationId, authUser.getUserId());

        return CommonApiResponse.success(
                response,
                ReservationSuccessMessage.GET_RESERVATION
        );
    }
}