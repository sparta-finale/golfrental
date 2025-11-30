package com.golfRental.domain.reservation.service.command;

import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;
import com.golfRental.domain.reservation.dto.response.ReservationUpdateStatusResponse;

public interface ReservationCommandService {

    ReservationCreateResponse createReservation(ReservationCreateRequest reservationCreateRequest, Long userId);

    ReservationUpdateStatusResponse approveReservation(Long reservationId, Long userId);

    ReservationUpdateStatusResponse rejectReservation(Long reservationId, Long userId);

    ReservationUpdateStatusResponse cancelReservation(Long reservationId, Long userId);
}
