package com.golfRental.domain.reservation.event;

import com.golfRental.domain.reservation.entity.Reservation;

public class ReturnRequestedEvent extends ReservationEvent {
    public ReturnRequestedEvent(Reservation reservation) {
        super(reservation);
    }
}
