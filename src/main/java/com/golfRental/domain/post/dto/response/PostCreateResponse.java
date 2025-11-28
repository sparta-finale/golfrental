package com.golfRental.domain.post.dto.response;

import com.golfRental.domain.post.enums.MethodOfReceiveReturn;
import com.golfRental.domain.post.enums.TradeStatus;
import com.golfRental.domain.user.entity.User;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PostCreateResponse(
        String title,
        String content,
        MethodOfReceiveReturn methodOfReceive,
        MethodOfReceiveReturn methodOfReturn,
        BigDecimal price,
        BigDecimal deposit,
        BigDecimal dailyRate,
        TradeStatus tradeStatus,
        User user
) {
}
