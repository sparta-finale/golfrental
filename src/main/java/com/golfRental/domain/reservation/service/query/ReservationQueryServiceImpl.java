package com.golfRental.domain.reservation.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.reservation.dto.response.ReservationGetAllResponse;
import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import com.golfRental.domain.reservation.dto.response.ReservationUpdateStatusResponse;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.exception.ReservationErrorCode;
import com.golfRental.domain.reservation.exception.ReservationException;
import com.golfRental.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;

    // 예약 상세 조회
    @Override
    public ReservationGetResponse findById(Long reservationId, Long currentUserId) {

        Reservation reservation = getReservationOrThrow(reservationId);

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

    // 예약 상세 조회 (내부용)
    @Override
    public Reservation findById(Long reservationId) {
        return getReservationOrThrow(reservationId);
    }

    // 공통 조회 메서드
    private Reservation getReservationOrThrow(Long reservationId) {
        return reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    // 사용자별 예약 목록 조회
    @Override
    public SliceResponse<ReservationGetAllResponse> getMyReservations(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Slice<Reservation> reservations =
                reservationRepository.findMyReservations(userId, pageable);

        Slice<ReservationGetAllResponse> contents = reservations.map(reservation ->
                ReservationGetAllResponse.builder()
                        .reservationId(reservation.getId())
                        .postId(reservation.getPost().getId())
                        .hostUserId(reservation.getHostUser().getId())
                        .guestUserId(reservation.getGuestUser().getId())
                        .reservationStartAt(reservation.getReservationStartAt())
                        .reservationEndAt(reservation.getReservationEndAt())
                        .price(reservation.getPrice())
                        .deposit(reservation.getDeposit())
                        .status(reservation.getStatus())
                        .build()
        );

        return SliceResponse.fromSlice(contents);
    }

    // 예약 상태 조회
    @Override
    public ReservationUpdateStatusResponse getReservationStatus(Long reservationId, Long userId) {

        Reservation reservation = reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

        // 호스트 또는 게스트만 조회 가능
        if (!reservation.getHostUser().getId().equals(userId)
                && !reservation.getGuestUser().getId().equals(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_FORBIDDEN);
        }

        return ReservationUpdateStatusResponse.builder()
                .reservationId(reservation.getId())
                .status(reservation.getStatus())
                .build();
    }
}
