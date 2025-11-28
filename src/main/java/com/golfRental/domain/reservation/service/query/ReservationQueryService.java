package com.golfRental.domain.reservation.service.query;

import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;

public interface ReservationQueryService {

    ReservationGetResponse findById(Long reservationId, Long currentUserId);
}
