package com.golfRental.domain.post.dto.response;

import com.golfRental.domain.post.entity.PostFavorites;
import com.golfRental.domain.post.enums.MethodOfReceiveReturn;
import com.golfRental.domain.post.enums.TradeStatus;

import java.math.BigDecimal;

public record PostGetByFavoritesResponse(
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
    public static PostGetByFavoritesResponse from(PostFavorites postFavorites, boolean favorites, PostImageResponse postImageResponse) {
        return new PostGetByFavoritesResponse(
                postFavorites.getPost().getId(), postFavorites.getPost().getTitle(), postFavorites.getPost().getContent(),
                postFavorites.getPost().getMethodOfReceive(), postFavorites.getPost().getMethodOfReturn(),
                postFavorites.getPost().getPrice(), postFavorites.getPost().getDeposit(), postFavorites.getPost().getDailyRate(),
                postFavorites.getPost().getTradeStatus(), postFavorites.getPost().getUser().getId(),
                postFavorites.getPost().getUser().getUsername(), postFavorites.getPost().getUser().getAddress(),
                postFavorites.getPost().getUser().getNickname(), postFavorites.getPost().getCategory().getId(),
                postFavorites.getPost().getCategory().getName(), favorites, postImageResponse
        );
    }
}
