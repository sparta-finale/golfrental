package com.golfRental.domain.post.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.category.entity.Category;
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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    // 검증 로직 성능 향상을 위한 필드
    private static final EnumSet<TradeStatus> RESERVABLE_STATUSES = EnumSet.of(TradeStatus.BEFORE_TRANSACTION, TradeStatus.TRADING);

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostImage> postImages = new ArrayList<>();

    @Builder
    private Post(
            String title, String content, MethodOfReceiveReturn methodOfReceive,
            MethodOfReceiveReturn methodOfReturn, BigDecimal price, BigDecimal deposit,
            BigDecimal dailyRate, User user, Category category
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
        this.category = category;
    }

    public static Post create(
            String title, String content, MethodOfReceiveReturn methodOfReceive,
            MethodOfReceiveReturn methodOfReturn, BigDecimal price, BigDecimal deposit,
            BigDecimal dailyRate, User user, Category category
    ) {
        return new Post(title, content, methodOfReceive, methodOfReturn, price, deposit, dailyRate, user, category);
    }

    public void update(PostUpdateRequest postUpdateRequest, Category category) {
        this.title = postUpdateRequest.title();
        this.content = postUpdateRequest.content();
        this.methodOfReceive = postUpdateRequest.methodOfReceive();
        this.methodOfReturn = postUpdateRequest.methodOfReturn();
        this.price = postUpdateRequest.price();
        this.deposit = postUpdateRequest.deposit();
        this.dailyRate = postUpdateRequest.dailyRate();
        this.category = category;
    }

    public void updateStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public boolean isOwnedBy(User user) {
        return user != null && this.user.getId().equals(user.getId());
    }

    public boolean isReservable() {
        return RESERVABLE_STATUSES.contains(this.tradeStatus);
    }
}
