package com.golfRental.domain.post.dto.response;

import com.golfRental.domain.post.enums.TradeStatus;
import lombok.Builder;

@Builder
public record PostUpdateStatusResponse(
        TradeStatus tradeStatus
) {
}
