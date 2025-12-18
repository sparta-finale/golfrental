package com.golfRental.domain.chatbot.service;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.service.query.PostQueryService;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PostEmbeddingService {

    private static final int EMBEDDING_BATCH_SIZE = 100;
    private static final String REDIS_INIT_KEY = "post-embeddings:initialized";

    private final PostQueryService postQueryService;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> postStore;
    private final RedisTemplate<String, String> redisTemplate;

    public PostEmbeddingService(
            PostQueryService postQueryService,
            EmbeddingModel embeddingModel,
            @Qualifier("postStore") EmbeddingStore<TextSegment> postStore,
            RedisTemplate<String, String> redisTemplate) {
        this.postQueryService = postQueryService;
        this.embeddingModel = embeddingModel;
        this.postStore = postStore;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        long startTime = System.currentTimeMillis();

        try {
            Boolean isInitialized = redisTemplate.hasKey(REDIS_INIT_KEY);

            if (Boolean.TRUE.equals(isInitialized)) {
                long duration = System.currentTimeMillis() - startTime;
                log.info("Post 임베딩 초기화 완료 - Redis 데이터 존재 ({}ms)", duration);
                return;
            }

            log.info("Post 임베딩 생성 시작");

            List<Post> posts = postQueryService.findAll();

            if (posts.isEmpty()) {
                log.warn("임베딩할 Post가 없습니다");
                return;
            }

            List<TextSegment> allSegments = posts.stream()
                    .map(this::convertToTextSegment)
                    .toList();

            int total = allSegments.size();

            for (int i = 0; i < total; i += EMBEDDING_BATCH_SIZE) {
                int end = Math.min(i + EMBEDDING_BATCH_SIZE, total);
                List<TextSegment> batchSegments = allSegments.subList(i, end);

                List<Embedding> batchEmbeddings = embeddingModel.embedAll(batchSegments).content();
                postStore.addAll(batchEmbeddings, batchSegments);
            }

            redisTemplate.opsForValue().set(REDIS_INIT_KEY, "true", 30, TimeUnit.DAYS);

            long duration = (System.currentTimeMillis() - startTime) / 1000;
            log.info("Post 임베딩 생성 완료 - 총 {}개, {}초", total, duration);

        } catch (Exception e) {
            log.error("Post 임베딩 실패", e);
        }
    }

    private TextSegment convertToTextSegment(Post post) {
        String text = String.format(
                "제목: %s\n내용: %s\n카테고리: %s\n가격: %s원\n보증금: %s원\n일일 대여료: %s원\n수령 방법: %s\n반납 방법: %s\n거래 상태: %s",
                post.getTitle(),
                post.getContent(),
                post.getCategory().getName(),
                post.getPrice(),
                post.getDeposit(),
                post.getDailyRate(),
                post.getMethodOfReceive(),
                post.getMethodOfReturn(),
                post.getTradeStatus()
        );

        Metadata metadata = new Metadata();
        metadata.put("postId", post.getId());

        return TextSegment.from(text, metadata);
    }
}