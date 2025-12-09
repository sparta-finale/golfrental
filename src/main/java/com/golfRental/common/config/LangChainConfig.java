package com.golfRental.common.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
public class LangChainConfig {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.model-name:gemini-2.0-flash}")
    private String modelName;

    @Value("${gemini.api.temperature:0.7}")
    private Double temperature;

    @Value("${gemini.api.max-tokens:2048}")
    private Integer maxTokens;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        log.info("Gemini Chat Model 초기화 - model: {}, temperature: {}", modelName, temperature);

        return GoogleAiGeminiChatModel.builder()
                .apiKey(geminiApiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxOutputTokens(maxTokens)
                .build();
    }

    @Bean("postStore")
    public EmbeddingStore<TextSegment> postStore() {
        log.info("Post Vector Store 초기화");

        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        log.info("Embedding Model 초기화 - All-MiniLM-L6-v2");

        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean("documentStore")
    public EmbeddingStore<TextSegment> documentStore() {
        log.info("Document Vector Store 초기화");

        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public Map<Object, ChatMemory> chatMemoryStore() {
        log.info("Chat Memory Store 초기화");

        return new ConcurrentHashMap<>();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider(
            Map<Object, ChatMemory> chatMemoryStore
    ) {
        log.info("Chat Memory Provider 초기화 - maxMessages: 10");

        return (ChatMemoryProvider) memoryId -> chatMemoryStore.computeIfAbsent(memoryId, id ->
                MessageWindowChatMemory.builder()
                        .maxMessages(10)  // 최대 10개 메시지 기억
                        .build()
        );
    }
}
