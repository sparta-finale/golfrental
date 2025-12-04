package com.golfRental.domain.reservation.event;

import com.golfRental.domain.reservation.entity.Reservation;
import lombok.Getter;

@Getter
public abstract class ReservationEvent {
    private final Reservation reservation;

    protected ReservationEvent(Reservation reservation) {
        this.reservation = reservation;
    }
}
