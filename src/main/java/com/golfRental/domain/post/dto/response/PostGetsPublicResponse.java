package com.golfRental.domain.post.dto.response;

import com.golfRental.domain.post.enums.MethodOfReceiveReturn;
import com.golfRental.domain.post.enums.TradeStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PostGetsPublicResponse(
        Long id,
        String title,
        String content,
        MethodOfReceiveReturn methodOfReceive,
        MethodOfReceiveReturn methodOfReturn,
        BigDecimal price,
        BigDecimal deposit,
        BigDecimal dailyRate,
        TradeStatus tradeStatus,
        Long userId,
        String username,
        String address,
        String nickname,
        Long categoryId,
        String categoryName,
        boolean favorites,
        List<PostImageResponse> images
) {
}
