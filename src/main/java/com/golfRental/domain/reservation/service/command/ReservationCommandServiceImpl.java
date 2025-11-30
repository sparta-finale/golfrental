package com.golfRental.domain.reservation.service.command;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.service.query.PostQueryService;
import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.enums.ReservationStatus;
import com.golfRental.domain.reservation.repository.ReservationRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final PostQueryService postQueryService;
    private final UserQueryService userQueryService;

    @Override
    public ReservationCreateResponse createReservation(ReservationCreateRequest request, Long userId) {

        Post post = postQueryService.findById(request.getPostId());
        User guestUser = userQueryService.findById(userId);
        User hostUser = post.getUser();

        Reservation reservation = Reservation.builder()
                .post(post)
                .hostUser(hostUser)
                .guestUser(guestUser)
                .reservationStartAt(request.getReservationStartAt())
                .reservationEndAt(request.getReservationEndAt())
                .price(request.getPrice())
                .deposit(request.getDeposit())
                .status(ReservationStatus.REQUESTED)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        return ReservationCreateResponse.builder()
                .reservationId(saved.getId())
                .postId(post.getId())
                .hostUserId(hostUser.getId())
                .guestUserId(guestUser.getId())
                .reservationStartAt(saved.getReservationStartAt())
                .reservationEndAt(saved.getReservationEndAt())
                .price(saved.getPrice())
                .deposit(saved.getDeposit())
                .status(saved.getStatus())
                .build();
    }
}
