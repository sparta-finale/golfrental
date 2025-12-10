package com.golfRental.domain.chatbot.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.chatbot.enums.ChatRole;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChatRole role;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Builder
    private ChatHistory(User user, ChatRole role, String message) {
        this.user = user;
        this.role = role;
        this.message = message;
    }

    public static ChatHistory createUserMessage(User user, String message) {
        return ChatHistory.builder()
                .user(user)
                .role(ChatRole.USER)
                .message(message)
                .build();
    }

    public static ChatHistory createAssistantMessage(User user, String message) {
        return ChatHistory.builder()
                .user(user)
                .role(ChatRole.ASSISTANT)
                .message(message)
                .build();
    }
}
