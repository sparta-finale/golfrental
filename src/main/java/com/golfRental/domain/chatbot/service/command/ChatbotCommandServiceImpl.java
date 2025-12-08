package com.golfRental.domain.chatbot.service.command;

import com.golfRental.domain.chatbot.dto.response.ChatbotMessageResponse;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatbotCommandServiceImpl implements ChatbotCommandService {

    private final ChatLanguageModel chatLanguageModel;
    private final EmbeddingModel embeddingModel;

    @Qualifier("postStore")
    private final EmbeddingStore<TextSegment> postStore;

    @Override
    public ChatbotMessageResponse chat(Long userId, String message) {
        log.info("챗봇 처리 시작 - userId: {}, message: {}", userId, message);

        try {
            // ContentRetriever 생성 (RAG 검색)
            EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(postStore)
                    .embeddingModel(embeddingModel)
                    .maxResults(5)
                    .minScore(0.6)
                    .build();

            // AI Assistant 생성
            GolfRentalAssistant assistant = AiServices.builder(GolfRentalAssistant.class)
                    .chatLanguageModel(chatLanguageModel)
                    .contentRetriever(retriever)  //RAG 검색 추가
                    .build();

            // AI 응답 생성
            String aiResponse = assistant.chat(message);

            return ChatbotMessageResponse.of(aiResponse);

        } catch (RuntimeException e) {
            log.error("챗봇 처리 중 오류 발생 - userId: {}", userId, e);

            String errorMessage = "죄송합니다. 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
            return ChatbotMessageResponse.of(errorMessage);
        }
    }

    interface GolfRentalAssistant {

        @SystemMessage("""
                당신은 골프 장비 렌탈 서비스 'golfRental'의 친절한 AI 어시스턴트입니다.
                
                역할:
                - 골프 장비 추천 및 검색
                - 렌탈 가격 안내
                - 장비 상세 정보 제공
                - 예약/취소/환불 정책 안내
                - 이용약관 설명
                
                말투: 친절하고 전문적이며 간결하게 답변하세요.
                """)
        String chat(@UserMessage String message);
    }
}
