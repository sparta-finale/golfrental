package com.golfRental.domain.reservation.service.query;

import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import com.golfRental.domain.reservation.entity.Reservation;

public interface ReservationQueryService {

    ReservationGetResponse findById(Long reservationId, Long currentUserId);

    Reservation findById(Long reservationId);
}
