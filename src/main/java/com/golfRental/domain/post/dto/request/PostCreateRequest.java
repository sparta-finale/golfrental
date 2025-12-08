package com.golfRental.domain.post.dto.request;

import com.golfRental.domain.post.enums.MethodOfReceiveReturn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 가능합니다.")
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

    @NotNull(message = "카테고리 아이디는 필수 입력값입니다.")
    private Long categoryId;

    @NotNull
    @Size(min = 1, max = 10, message = "이미지는 최소 1개, 최대 10개까지 등록 가능합니다.")
    private List<PostImageInfoCreateRequest> images;

    public record PostImageInfoCreateRequest(
            @NotNull Long imageId,
            @NotNull Boolean isThumbnail,
            @NotNull @Min(0) Integer sortOrder
    ) {
    }
}
