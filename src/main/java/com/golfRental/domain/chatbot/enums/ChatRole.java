package com.golfRental.domain.chatbot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRole {
    USER("사용자"),
    ASSISTANT("AI 어시스턴트");

    private final String displayName;
}
