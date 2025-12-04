package com.golfRental.domain.reservation.event;

import com.golfRental.domain.reservation.entity.Reservation;

public class RentalStartedEvent extends ReservationEvent {
    public RentalStartedEvent(Reservation reservation) {
        super(reservation);
    }
}
