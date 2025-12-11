package com.golfRental.domain.post.dto.response;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.enums.MethodOfReceiveReturn;
import com.golfRental.domain.post.enums.TradeStatus;

import java.math.BigDecimal;

public record PostGetByCategoryResponse(
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
        PostImageResponse image
) {
    public static PostGetByCategoryResponse from(Post post, boolean favorites, PostImageResponse postImageResponse) {
        return new PostGetByCategoryResponse(
                post.getId(), post.getTitle(), post.getContent(), post.getMethodOfReceive(), post.getMethodOfReturn(),
                post.getPrice(), post.getDeposit(), post.getDailyRate(), post.getTradeStatus(), post.getUser().getId(),
                post.getUser().getUsername(), post.getUser().getAddress(), post.getUser().getNickname(),
                post.getCategory().getId(), post.getCategory().getName(), favorites, postImageResponse
        );
    }
}
