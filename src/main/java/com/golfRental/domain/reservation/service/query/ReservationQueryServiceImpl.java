package com.golfRental.domain.reservation.service.query;

import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.exception.ReservationErrorCode;
import com.golfRental.domain.reservation.exception.ReservationException;
import com.golfRental.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationGetResponse findById(Long reservationId, Long currentUserId) {

        Reservation reservation = reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

        // 권한 검증
        if (!reservation.getHostUser().getId().equals(currentUserId) &&
                !reservation.getGuestUser().getId().equals(currentUserId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_FORBIDDEN);
        }

        return ReservationGetResponse.builder()
                .reservationId(reservation.getId())
                .postId(reservation.getPost().getId())
                .hostUserId(reservation.getHostUser().getId())
                .guestUserId(reservation.getGuestUser().getId())
                .reservationStartAt(reservation.getReservationStartAt())
                .reservationEndAt(reservation.getReservationEndAt())
                .price(reservation.getPrice())
                .deposit(reservation.getDeposit())
                .status(reservation.getStatus())
                .build();
    }

    @Override
    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }
}