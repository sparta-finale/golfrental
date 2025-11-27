package com.golfRental.domain.post.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.post.enums.MethodOfReceiveReturn;
import com.golfRental.domain.post.enums.TradeStatus;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private MethodOfReceiveReturn methodOfReceive;

    private MethodOfReceiveReturn methodOfReturn;

    private BigDecimal price;

    private BigDecimal deposit;

    private BigDecimal dailyRate;

    private TradeStatus tradeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Category category;

    @Builder
    public Post(
            String title, String content, MethodOfReceiveReturn methodOfReceive,
            MethodOfReceiveReturn methodOfReturn, BigDecimal price, BigDecimal deposit,
            BigDecimal dailyRate, User user
    ) {
        this.title = title;
        this.content = content;
        this.methodOfReceive = methodOfReceive;
        this.methodOfReturn = methodOfReturn;
        this.price = price;
        this.deposit = deposit;
        this.dailyRate = dailyRate;
        this.tradeStatus = TradeStatus.BEFORE_TRANSACTION;
        this.user = user;
    }
}
