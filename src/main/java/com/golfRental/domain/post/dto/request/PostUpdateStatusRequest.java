package com.golfRental.domain.post.dto.request;

import com.golfRental.domain.post.enums.TradeStatus;
import lombok.Getter;

@Getter
public class PostUpdateStatusRequest {
    TradeStatus tradeStatus;
}
