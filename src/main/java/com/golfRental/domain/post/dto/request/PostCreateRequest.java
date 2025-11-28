package com.golfRental.domain.post.dto.request;

import com.golfRental.domain.post.enums.MethodOfReceiveReturn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 최대 50자까지 가능합니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력값입니다.")
    @Size(max = 1000, message = "내용은 최대 1000자까지 가능합니다.")
    private String content;

    @NotNull(message = "수령 방식은 필수 입력값입니다.")
    private MethodOfReceiveReturn methodOfReceive;

    @NotNull(message = "반납 방식은 필수 입력값입니다.")
    private MethodOfReceiveReturn methodOfReturn;

    @NotNull(message = "대여 금액은 필수 입력값입니다.")
    @Min(value = 0, message = "금액은 0원 이상 입력해야 합니다.")
    private BigDecimal price;

    @NotNull(message = "보증금은 필수 입력값입니다.")
    @Min(value = 0, message = "금액은 0원 이상 입력해야 합니다.")
    private BigDecimal deposit;

    @NotNull(message = "일일 이용료는 필수 입력값입니다.")
    @Min(value = 0, message = "금액은 0원 이상 입력해야 합니다.")
    private BigDecimal dailyRate;
}
