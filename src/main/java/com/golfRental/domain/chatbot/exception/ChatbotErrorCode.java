package com.golfRental.domain.chatbot.exception;

import com.golfRental.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatbotErrorCode implements ErrorCode {

    AI_MODEL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CHATBOT-5001", "AI 모델 처리 중 오류가 발생했습니다"),
    AI_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "CHATBOT-5002", "AI 응답 시간이 초과되었습니다"),
    AI_RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "CHATBOT-5003", "AI 요청 한도를 초과했습니다"),

    // 임베딩 관련 에러 (5100번대)
    EMBEDDING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CHATBOT-5101", "임베딩 처리 중 오류가 발생했습니다"),
    VECTOR_SEARCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CHATBOT-5102", "벡터 검색 중 오류가 발생했습니다"),

    // 문서 인덱싱 관련 에러 (5200번대)
    DOCUMENT_LOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CHATBOT-5201", "문서 로드 중 오류가 발생했습니다"),
    DOCUMENT_INDEXING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CHATBOT-5202", "문서 인덱싱 중 오류가 발생했습니다"),

    // 대화 히스토리 관련 에러 (5300번대)
    CHAT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CHATBOT-5301", "대화 히스토리를 찾을 수 없습니다"),
    CHAT_HISTORY_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CHATBOT-5302", "대화 히스토리 저장 중 오류가 발생했습니다"),

    // Memory 관련 에러 (5400번대)
    MEMORY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CHATBOT-5401", "대화 메모리 처리 중 오류가 발생했습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
