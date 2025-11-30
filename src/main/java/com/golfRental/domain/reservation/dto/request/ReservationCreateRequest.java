package com.golfRental.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReservationCreateRequest {

    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long postId;

    @NotNull(message = "예약 시작일은 필수입니다.")
    private String reservationStartAt;

    @NotNull(message = "예약 종료일은 필수입니다.")
    private String reservationEndAt;

    @NotNull(message = "가격은 필수입니다.")
    private Integer price;

    @NotNull(message = "보증금은 필수입니다.")
    private Integer deposit;
}
