package com.golfRental.domain.post.dto.response;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.enums.TradeStatus;

public record PostUpdateStatusResponse(
        TradeStatus tradeStatus
) {
    public static PostUpdateStatusResponse from(Post post) {
        return new PostUpdateStatusResponse(post.getTradeStatus());
    }
}
