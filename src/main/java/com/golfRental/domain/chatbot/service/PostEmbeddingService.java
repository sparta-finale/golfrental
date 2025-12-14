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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostEmbeddingService {

    private static final int EMBEDDING_BATCH_SIZE = 100;
    private final PostQueryService postQueryService;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> postStore;

    public PostEmbeddingService(
            PostQueryService postQueryService,
            EmbeddingModel embeddingModel,
            @Qualifier("postStore") EmbeddingStore<TextSegment> postStore) {
        this.postQueryService = postQueryService;
        this.embeddingModel = embeddingModel;
        this.postStore = postStore;
    }

    @PostConstruct
    public void init() {
        long startTime = System.currentTimeMillis();

        try {
            // 1. 전체 Post 조회 (이미 Fetch Join 적용됨)
            List<Post> posts = postQueryService.findAll();
            log.info("Post 조회 완료 - 총 {}개", posts.size());

            if (posts.isEmpty()) {
                log.warn("임베딩할 Post가 없습니다");
                return;
            }

            // 2. TextSegment 변환
            log.info("TextSegment 변환 시작");
            List<TextSegment> allSegments = posts.stream()
                    .map(this::convertToTextSegment)
                    .toList();
            log.info("TextSegment 변환 완료 - {}개", allSegments.size());

            // 3. Chunk Batch 처리 (100개씩)

            int total = allSegments.size();

            log.info("Chunk Batch 임베딩 시작 - Batch Size: {}, 총 {}회 호출 예정",
                    EMBEDDING_BATCH_SIZE, (total + EMBEDDING_BATCH_SIZE - 1) / EMBEDDING_BATCH_SIZE);

            for (int i = 0; i < total; i += EMBEDDING_BATCH_SIZE) {
                int end = Math.min(i + EMBEDDING_BATCH_SIZE, total);
                List<TextSegment> batchSegments = allSegments.subList(i, end);

                // Batch 임베딩 (100개씩)
                List<Embedding> batchEmbeddings = embeddingModel.embedAll(batchSegments).content();

                // Vector Store 저장
                postStore.addAll(batchEmbeddings, batchSegments);

                log.info("Batch 처리 진행률: {}/{} ({} ~ {})",
                        end, total, i + 1, end);
            }

            long endTime = System.currentTimeMillis();
            long durationMs = endTime - startTime;
            long durationSec = durationMs / 1000;
            log.info("Post 임베딩 완료. 총 {}개, 소요 시간: {}초", total, durationSec);

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