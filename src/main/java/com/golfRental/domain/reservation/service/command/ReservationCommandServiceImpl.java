package com.golfRental.domain.reservation.service.command;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.service.query.PostQueryService;
import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;
import com.golfRental.domain.reservation.dto.response.ReservationUpdateStatusResponse;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.reservation.enums.ReservationStatus;
import com.golfRental.domain.reservation.event.*;
import com.golfRental.domain.reservation.exception.ReservationErrorCode;
import com.golfRental.domain.reservation.exception.ReservationException;
import com.golfRental.domain.reservation.repository.ReservationRepository;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private static final long LOCK_WAIT_TIME_SECONDS = 3L;
    private static final long LOCK_LEASE_TIME_SECONDS = 5L;
    private final ReservationRepository reservationRepository;
    private final PostQueryService postQueryService;
    private final UserQueryService userQueryService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedissonClient redissonClient;

    // 예약 생성
    @Override
    public ReservationCreateResponse createReservation(ReservationCreateRequest request, Long userId) {

        String lockKey = "reservation:post:" + request.postId();
        RLock lock = redissonClient.getLock(lockKey);

        try {

            boolean available = lock.tryLock(LOCK_WAIT_TIME_SECONDS, LOCK_LEASE_TIME_SECONDS, TimeUnit.SECONDS);

            if (!available) {
                log.warn("예약 락 획득 실패 - postId: {}, userId: {}", request.postId(), userId);
                throw new ReservationException(ReservationErrorCode.LOCK_ACQUISITION_FAILED);
            }

            log.info("예약 락 획득 성공 - postId: {}, lockKey: {}", request.postId(), lockKey);

            Post post = postQueryService.findById(request.postId());
            User guestUser = userQueryService.findById(userId);
            User hostUser = post.getUser();

            // 검증 로직 시작
            validateReservationCreation(post, guestUser, request);

            Reservation reservation = Reservation.builder()
                    .post(post)
                    .hostUser(hostUser)
                    .guestUser(guestUser)
                    .reservationStartAt(request.reservationStartAt())
                    .reservationEndAt(request.reservationEndAt())
                    .price(request.price())
                    .deposit(request.deposit())
                    .status(ReservationStatus.REQUESTED)
                    .build();

            Reservation saved = reservationRepository.save(reservation);

            // 예약 생성 이벤트 발행
            eventPublisher.publishEvent(new ReservationCreatedEvent(saved));

            return ReservationCreateResponse.from(saved);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("예약 락 인터럽트 - postId: {}, userId: {}", request.postId(), userId, e);
            throw new ReservationException(ReservationErrorCode.LOCK_INTERRUPTED);

        } finally {

            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("예약 락 해제 완료 - postId: {}, lockKey: {}", request.postId(), lockKey);

            }
        }
    }

    // 승인
    @Override
    public ReservationUpdateStatusResponse approveReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyHost(reservationId, userId);

        reservation.approve();

        // 예약 승인 이벤트 발행
        eventPublisher.publishEvent(new ReservationApprovedEvent(reservation));

        return ReservationUpdateStatusResponse.from(reservation);
    }

    // 거절
    @Override
    public ReservationUpdateStatusResponse rejectReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyHost(reservationId, userId);

        reservation.reject();

        // 예약 거절 이벤트 발행
        eventPublisher.publishEvent(new ReservationRejectedEvent(reservation));

        return ReservationUpdateStatusResponse.from(reservation);
    }

    // 취소
    @Override
    public ReservationUpdateStatusResponse cancelReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyGuest(reservationId, userId);

        reservation.cancel();

        // 예약 취소 이벤트 발행
        eventPublisher.publishEvent(new ReservationCancelledEvent(reservation));

        return ReservationUpdateStatusResponse.from(reservation);
    }

    // 대여 시작
    @Override
    public ReservationUpdateStatusResponse startReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyHost(reservationId, userId);

        reservation.startRental();

        // 대여 시작 이벤트 발행
        eventPublisher.publishEvent(new RentalStartedEvent(reservation));

        return ReservationUpdateStatusResponse.from(reservation);
    }

    // 반납 요청
    @Override
    public ReservationUpdateStatusResponse requestReturn(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyGuest(reservationId, userId);

        reservation.requestReturn();

        // 반납 요청 이벤트 발행
        eventPublisher.publishEvent(new ReturnRequestedEvent(reservation));

        return ReservationUpdateStatusResponse.from(reservation);
    }

    // 반납 완료
    @Override
    public ReservationUpdateStatusResponse completeReservation(Long reservationId, Long userId) {

        Reservation reservation = findReservationAndVerifyHost(reservationId, userId);

        reservation.complete();

        // 반납 완료 이벤트 발행
        eventPublisher.publishEvent(new ReturnCompletedEvent(reservation));

        return ReservationUpdateStatusResponse.from(reservation);
    }

    // 공통 조회 메서드
    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findByIdWithDetails(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    // 호스트 검증
    private Reservation findReservationAndVerifyHost(Long reservationId, Long userId) {

        Reservation reservation = findReservationById(reservationId);

        if (!reservation.getHostUser().getId().equals(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_FORBIDDEN);
        }

        return reservation;
    }

    // 게스트 검증
    private Reservation findReservationAndVerifyGuest(Long reservationId, Long userId) {

        Reservation reservation = findReservationById(reservationId);

        if (!reservation.getGuestUser().getId().equals(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_FORBIDDEN);
        }

        return reservation;
    }

    // 예약 생성 전 검증 수행
    private void validateReservationCreation(Post post, User guestUser, ReservationCreateRequest request) {

        // 자기 자신의 게시글 예약 방지
        if (post.isOwnedBy(guestUser)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_SELF_BOOKING_NOT_ALLOWED);
        }

        // 예약 날짜 유효성 (시작일 < 종료일)
        if (!request.reservationStartAt().isBefore(request.reservationEndAt())) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_INVALID_DATE_RANGE);
        }

        // Post 상태 확인 (BEFORE_TRANSACTION 또는 TRADING만 허용)
        if (!post.isReservable()) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_POST_NOT_AVAILABLE);
        }

        // 중복 예약 방지
        boolean hasConflict = reservationRepository.existsConflictingReservation(
                post.getId(),
                request.reservationStartAt(),
                request.reservationEndAt()
        );

        if (hasConflict) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_DATE_CONFLICT);
        }
    }
}