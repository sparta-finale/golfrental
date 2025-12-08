package com.golfRental.domain.chatbot.service;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.service.query.PostQueryService;
import dev.langchain4j.data.document.Metadata;
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
        try {
            List<Post> posts = postQueryService.findAll();

            for (Post post : posts) {
                TextSegment segment = convertToTextSegment(post);
                postStore.add(embeddingModel.embed(segment).content(), segment);
            }
            log.info("Post 임베딩 완료 - 총 {}개", posts.size());

        } catch (RuntimeException e) {
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
