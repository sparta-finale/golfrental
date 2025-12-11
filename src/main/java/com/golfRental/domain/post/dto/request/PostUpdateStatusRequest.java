package com.golfRental.domain.post.dto.request;

import com.golfRental.domain.post.enums.TradeStatus;
import jakarta.validation.constraints.NotNull;

public record PostUpdateStatusRequest(
        @NotNull(message = "거래 상태 변경을 위한 필수 값입니다.")
        TradeStatus tradeStatus
) {
}
