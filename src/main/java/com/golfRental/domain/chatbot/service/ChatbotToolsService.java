package com.golfRental.domain.chatbot.service;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.service.query.PostQueryService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotToolsService {

    private final PostQueryService postQueryService;
    private final EmbeddingModel embeddingModel;

    @Qualifier("postStore")
    private final EmbeddingStore<TextSegment> postStore;

    @Qualifier("documentStore")
    private final EmbeddingStore<TextSegment> documentStore;

    @Value("${rag.retriever.post.max-results:5}")
    private int postMaxResults;

    @Value("${rag.retriever.post.min-score:0.6}")
    private double postMinScore;

    @Value("${rag.retriever.document.max-results:3}")
    private int documentMaxResults;

    @Value("${rag.retriever.document.min-score:0.7}")
    private double documentMinScore;

    @Tool("골프 장비 렌탈 게시글을 검색합니다. 제목, 내용, 카테고리, 가격 등의 조건으로 검색할 수 있습니다.")
    public String searchPost(@P("검색 쿼리 (예: 타이틀리스트 드라이버, 15000원 이하 아이언)") String query) {
        log.info("Tool 호출 - searchPost: {}", query);

        try {
            // 쿼리 임베딩
            Embedding queryEmbedding = embeddingModel.embed(query).content();

            // 벡터 검색 (설정값 사용)
            EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .maxResults(postMaxResults)
                    .minScore(postMinScore)
                    .build();

            EmbeddingSearchResult<TextSegment> searchResult = postStore.search(request);
            List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

            if (matches.isEmpty()) {
                return "검색 조건에 맞는 게시글을 찾을 수 없습니다.";
            }

            // 검색 결과를 자연어로 변환
            StringBuilder result = new StringBuilder("검색된 게시글 목록:\n\n");

            for (int i = 0; i < matches.size(); i++) {
                TextSegment segment = matches.get(i).embedded();
                result.append(String.format("%d. %s\n", i + 1, segment.text()));
            }

            log.info("게시글 검색 완료 - 결과: {}개, maxResults: {}, minScore: {}",
                    matches.size(), postMaxResults, postMinScore);

            return result.toString();

        } catch (RuntimeException e) {
            log.error("게시글 검색 중 오류 발생", e);
            return "게시글 검색 중 오류가 발생했습니다.";
        }
    }

    @Tool("예약, 취소, 환불, 이용약관 등의 정책을 검색합니다.")
    public String searchPolicy(@P("질문 (예: 예약 취소는 어떻게 하나요?)") String question) {
        log.info("Tool 호출 - searchPolicy: {}", question);

        try {
            // 질문 임베딩
            Embedding queryEmbedding = embeddingModel.embed(question).content();

            // 벡터 검색 (설정값 사용)
            EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .maxResults(documentMaxResults)
                    .minScore(documentMinScore)
                    .build();

            EmbeddingSearchResult<TextSegment> searchResult = documentStore.search(searchRequest);
            List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

            if (matches.isEmpty()) {
                return "관련된 정책 정보를 찾을 수 없습니다. 고객센터로 문의해주세요.";
            }

            // 검색 결과를 자연어로 변환
            StringBuilder result = new StringBuilder("관련 정책:\n\n");

            for (int i = 0; i < matches.size(); i++) {
                TextSegment segment = matches.get(i).embedded();
                result.append(String.format("%s\n\n", segment.text()));
            }

            log.info("정책 검색 완료 - 결과: {}개, maxResults: {}, minScore: {}",
                    matches.size(), documentMaxResults, documentMinScore);

            return result.toString();

        } catch (Exception e) {
            log.error("정책 검색 중 오류 발생", e);
            return "정책 검색 중 오류가 발생했습니다.";
        }
    }

    @Tool("특정 카테고리의 렌탈 가격 통계를 조회합니다.")
    public String getPriceStatistics(@P("카테고리명 (예: 드라이버, 아이언, 퍼터, 웨지, 우드)") String categoryName) {
        log.info("Tool 호출 - getPriceStatistics: {}", categoryName);

        try {
            // 카테고리명으로 게시글 필터링
            List<Post> posts = postQueryService.findAllByCategoryName(categoryName);

            if (posts.isEmpty()) {
                return String.format("%s 카테고리의 게시글이 없습니다.", categoryName);
            }

            // 가격 통계 계산
            BigDecimal avgPrice = posts.stream()
                    .map(Post::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(posts.size()), 0, BigDecimal.ROUND_HALF_UP);

            BigDecimal minPrice = posts.stream()
                    .map(Post::getPrice)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            BigDecimal maxPrice = posts.stream()
                    .map(Post::getPrice)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            String result = String.format("""
                            %s 카테고리 렌탈 가격 통계:
                            - 평균: %,d원
                            - 최저: %,d원
                            - 최고: %,d원
                            - 등록 수: %d개
                            """, categoryName, avgPrice.intValue(), minPrice.intValue(),
                    maxPrice.intValue(), posts.size());

            log.info("가격 통계 조회 완료 - category: {}", categoryName);

            return result;

        } catch (RuntimeException e) {
            log.error("가격 통계 조회 중 오류 발생", e);
            return "가격 통계 조회 중 오류가 발생했습니다.";
        }
    }
}

