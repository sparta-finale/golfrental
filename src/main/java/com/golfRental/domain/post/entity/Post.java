package com.golfRental.domain.post.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.post.dto.request.PostUpdateRequest;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MethodOfReceiveReturn methodOfReceive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MethodOfReceiveReturn methodOfReturn;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal deposit;

    @Column(nullable = false)
    private BigDecimal dailyRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus tradeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Category category;

    @Builder
    private Post(
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

    public void update(PostUpdateRequest postUpdateRequest) {
        this.title = postUpdateRequest.getTitle();
        this.content = postUpdateRequest.getContent();
        this.methodOfReceive = postUpdateRequest.getMethodOfReceive();
        this.methodOfReturn = postUpdateRequest.getMethodOfReturn();
        this.price = postUpdateRequest.getPrice();
        this.deposit = postUpdateRequest.getDeposit();
        this.dailyRate = postUpdateRequest.getDailyRate();
    }

    public void updateStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
}
