package com.golfRental.domain.reservation.message;

public final class ReservationSuccessMessage {

    public final static String RESERVATION_CREATED = "예약을 요청을 생성하였습니다.";
    public final static String GET_RESERVATION = "예약 조회에 성공하였습니다.";
    public final static String GET_RESERVATION_LIST = "사용자 예약 목록 조회에 성공하였습니다.";
    public final static String RESERVATION_APPROVED = "예약이 승인되었습니다.";
    public final static String RESERVATION_REJECTED = "예약이 거절되었습니다.";
    public final static String RESERVATION_CANCELLED = "예약이 취소되었습니다.";
    public final static String RESERVATION_RENTAL_STARTED = "대여가 시작되었습니다.";

    private ReservationSuccessMessage() {
    }
}
