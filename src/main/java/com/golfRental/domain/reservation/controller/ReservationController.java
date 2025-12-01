package com.golfRental.domain.reservation.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.reservation.dto.request.ReservationCreateRequest;
import com.golfRental.domain.reservation.dto.response.ReservationCreateResponse;
import com.golfRental.domain.reservation.dto.response.ReservationGetAllResponse;
import com.golfRental.domain.reservation.dto.response.ReservationGetResponse;
import com.golfRental.domain.reservation.dto.response.ReservationUpdateStatusResponse;
import org.springframework.http.ResponseEntity;

public interface ReservationController {

    /**
     * 예약 생성
     *
     * @param request  예약 생성 요청 DTO
     * @param authUser 로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationCreateResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationCreateResponse>> createReservation(
            ReservationCreateRequest request,
            AuthUser authUser
    );

    /**
     * 예약 단건 조회
     *
     * @param reservationId 조회할 예약 ID
     * @param authUser      로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationGetResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationGetResponse>> getReservation(
            Long reservationId,
            AuthUser authUser
    );

    /**
     * 사용자별 예약 목록 조회
     *
     * @param authUser 로그인 사용자 정보
     * @param page     페이지 번호
     * @param size     페이지 크기
     * @return ResponseEntity<CommonApiResponse < SliceResponse < ReservationGetAllResponse>>>
     */
    ResponseEntity<CommonApiResponse<SliceResponse<ReservationGetAllResponse>>> getMyReservations(
            AuthUser authUser,
            int page,
            int size
    );

    /**
     * 예약 승인
     *
     * @param reservationId 승인할 예약 ID
     * @param authUser      로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationUpdateStatusResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> approveReservation(
            Long reservationId,
            AuthUser authUser
    );

    /**
     * 예약 거절
     *
     * @param reservationId 거절할 예약 ID
     * @param authUser      로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationUpdateStatusResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> rejectReservation(
            Long reservationId,
            AuthUser authUser
    );

    /**
     * 예약 취소
     *
     * @param reservationId 취소할 예약 ID
     * @param authUser      로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationUpdateStatusResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> cancelReservation(
            Long reservationId,
            AuthUser authUser
    );

    /**
     * 대여 시작
     *
     * @param reservationId 대여를 시작할 예약 ID
     * @param authUser      로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationUpdateStatusResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> startReservation(
            Long reservationId,
            AuthUser authUser
    );

    /**
     * 반납 요청
     *
     * @param reservationId 반납을 요청할 예약 ID
     * @param authUser      로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationUpdateStatusResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> requestReturn(
            Long reservationId,
            AuthUser authUser
    );

    /**
     * 반납 승인(완료)
     *
     * @param reservationId 반납 완료 처리할 예약 ID
     * @param authUser      로그인 사용자 정보
     * @return ResponseEntity<CommonApiResponse < ReservationUpdateStatusResponse>>
     */
    ResponseEntity<CommonApiResponse<ReservationUpdateStatusResponse>> completeReservation(
            Long reservationId,
            AuthUser authUser
    );
}