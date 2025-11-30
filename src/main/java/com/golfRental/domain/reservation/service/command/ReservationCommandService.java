package com.golfRental.domain.reservation.service.command;

import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;

public interface ReservationCommandService {

    ReservationCreateResponse createReservation(ReservationCreateRequest reservationCreateRequest, Long userId);
}
