package com.golfRental.domain.post.dto.response;

import com.golfRental.domain.category.entity.Category;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.enums.MethodOfReceiveReturn;
import com.golfRental.domain.post.enums.TradeStatus;
import com.golfRental.domain.user.entity.User;

import java.math.BigDecimal;
import java.util.List;

public record PostCreateResponse(
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
        Boolean favorites,
        List<PostImageResponse> images
) {
    public static PostCreateResponse from(Post post, User user, Category category, List<PostImageResponse> imagesResponse) {
        return new PostCreateResponse(
                post.getId(), post.getTitle(), post.getContent(), post.getMethodOfReceive(), post.getMethodOfReturn(),
                post.getPrice(), post.getDeposit(), post.getDailyRate(), post.getTradeStatus(), user.getId(), user.getUsername(),
                user.getAddress(), user.getNickname(), category.getId(), category.getName(), false, imagesResponse
        );
    }
}
