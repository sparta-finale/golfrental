package com.golfRental.domain.chatbot.service.command;

import com.golfRental.domain.chatbot.dto.response.ChatbotMessageResponse;
import com.golfRental.domain.chatbot.entity.ChatHistory;
import com.golfRental.domain.chatbot.exception.ChatbotErrorCode;
import com.golfRental.domain.chatbot.exception.ChatbotException;
import com.golfRental.domain.chatbot.repository.ChatHistoryRepository;
import com.golfRental.domain.chatbot.service.ChatbotToolsService;
import com.golfRental.domain.user.entity.User;
import com.golfRental.domain.user.service.query.UserQueryService;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatbotCommandServiceImpl implements ChatbotCommandService {
    
    private final ChatLanguageModel chatLanguageModel;
    private final ChatMemoryProvider chatMemoryProvider;
    private final ChatbotToolsService chatbotToolsService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserQueryService userQueryService;

    private GolfRentalAssistant assistant;

    @PostConstruct
    public void initAssistant() {
        this.assistant = AiServices.builder(GolfRentalAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .tools(chatbotToolsService)
                .build();
    }

    @Override
    public ChatbotMessageResponse chat(Long userId, String message) {
        User user = null;

        try {
            user = userQueryService.findById(userId);

            saveChatHistory(user, message, true);

            String aiResponse = generateAiResponse(userId, message);

            saveChatHistory(user, aiResponse, false);

            log.info("챗봇 처리 완료 - userId: {}, responseLength: {}", userId, aiResponse.length());

            return ChatbotMessageResponse.of(aiResponse);

        } catch (RuntimeException e) {
            log.error("챗봇 처리 중 예상치 못한 오류 - userId: {}", userId, e);

            String errorMessage = "죄송합니다. 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";

            if (user != null) {
                try {
                    saveChatHistory(user, errorMessage, false);
                } catch (RuntimeException saveError) {
                    log.error("에러 메시지 저장 실패 - userId: {}", userId, saveError);
                }
            }

            return ChatbotMessageResponse.of(errorMessage);
        }
    }

    private String generateAiResponse(Long userId, String message) {
        try {
            // AI 응답 생성 (초기화된 assistant 재사용)
            String aiResponse = assistant.chat(userId, message);

            if (aiResponse == null || aiResponse.isBlank()) {
                log.error("AI 응답이 비어있음 - userId: {}", userId);
                throw new ChatbotException(ChatbotErrorCode.AI_MODEL_ERROR);
            }

            return aiResponse;

        } catch (ChatbotException e) {
            throw e;

        } catch (RuntimeException e) {
            log.error("AI 응답 생성 실패 - userId: {}", userId, e);

            // 타임아웃 여부 체크
            if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                throw new ChatbotException(ChatbotErrorCode.AI_TIMEOUT);
            }

            // Rate Limit 체크
            if (e.getMessage() != null && e.getMessage().contains("rate limit")) {
                throw new ChatbotException(ChatbotErrorCode.AI_RATE_LIMIT_EXCEEDED);
            }

            throw new ChatbotException(ChatbotErrorCode.AI_MODEL_ERROR);
        }
    }

    private void saveChatHistory(User user, String message, boolean isUserMessage) {
        try {
            ChatHistory chatHistory = isUserMessage
                    ? ChatHistory.createUserMessage(user, message)
                    : ChatHistory.createAssistantMessage(user, message);

            chatHistoryRepository.save(chatHistory);

            log.debug("{} 메시지 저장 완료 - userId: {}",
                    isUserMessage ? "사용자" : "AI", user.getId());

        } catch (RuntimeException e) {
            log.error("대화 히스토리 저장 실패 - userId: {}, isUserMessage: {}",
                    user.getId(), isUserMessage, e);
            throw new ChatbotException(ChatbotErrorCode.CHAT_HISTORY_SAVE_ERROR);
        }
    }


    interface GolfRentalAssistant {

        @dev.langchain4j.service.SystemMessage("""
                당신은 골프 장비 렌탈 서비스 'golfRental'의 친절한 AI 어시스턴트입니다.
                
                역할:
                - 골프 장비 추천 및 검색
                - 렌탈 가격 안내
                - 장비 상세 정보 제공
                - 예약/취소/환불 정책 안내
                - 이용약관 설명
                
                도구 사용:
                - 장비 정보가 필요하면 searchEquipment 도구를 사용하세요
                - 정책/FAQ가 필요하면 searchPolicy 도구를 사용하세요
                - 가격 통계가 필요하면 getPriceStatistics 도구를 사용하세요
                
                말투: 친절하고 전문적이며 간결하게 답변하세요.
                
                중요: 
                - 도구에서 받은 정보를 바탕으로 정확하게 답변하세요.
                - 정책 관련 답변 시 출처(예: FAQ, 이용약관 제5조)를 명시하세요.
                - 확실하지 않은 내용은 추측하지 말고 "확인이 필요합니다"라고 답하세요.
                - 이전 대화 내용을 기억하고 맥락에 맞게 답변하세요.
                - 응답은 간결하고 명확하게 작성하세요 (최대 500자).
                """)
        String chat(@MemoryId Object memoryId,
                    @UserMessage String message);
    }
}
