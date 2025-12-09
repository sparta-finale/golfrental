package com.golfRental.domain.chatbot.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentIndexingService {

    private final EmbeddingModel embeddingModel;

    @Qualifier("documentStore")
    private final EmbeddingStore<TextSegment> documentStore;

    @PostConstruct
    public void indexAllDocuments() {
        log.info("문서 파일 인덱싱 시작");

        try {
            // documents 폴더에서 모든 .md 파일 로드
            List<Document> documents = loadMarkdownDocuments();

            if (documents.isEmpty()) {
                log.warn("인덱싱할 문서가 없습니다");
                return;
            }

            // 문서 분할 (300자 단위, 오버랩 없음)
            DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);

            List<TextSegment> allSegments = new ArrayList<>();

            for (Document doc : documents) {
                List<TextSegment> segments = splitter.split(doc);
                allSegments.addAll(segments);
                log.info("문서 분할 완료 - 파일: {}, 청크: {}개",
                        doc.metadata().getString("file_name"), segments.size());
            }

            // 임베딩 생성
            List<Embedding> embeddings = embeddingModel.embedAll(allSegments)
                    .content();

            // Vector Store에 저장
            documentStore.addAll(embeddings, allSegments);

            log.info("문서 인덱싱 완료 - 총 {}개 문서, {}개 청크",
                    documents.size(), allSegments.size());

        } catch (Exception e) {
            log.error("문서 인덱싱 중 오류 발생", e);
        }
    }

    private List<Document> loadMarkdownDocuments() throws IOException {
        List<Document> documents = new ArrayList<>();

        // PathMatchingResourcePatternResolver로 classpath의 문서 찾기
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:documents/*.md");

        TextDocumentParser parser = new TextDocumentParser();

        for (Resource resource : resources) {
            try {
                String content = new String(resource.getInputStream().readAllBytes());
                Document doc = Document.from(content);

                // 메타데이터에 파일명 추가
                doc.metadata().put("file_name", resource.getFilename());
                doc.metadata().put("source", "document");

                documents.add(doc);

                log.info("문서 로드 완료 - 파일: {}", resource.getFilename());

            } catch (Exception e) {
                log.error("문서 로드 실패 - 파일: {}", resource.getFilename(), e);
            }
        }

        return documents;
    }

    public void indexDocument(Path filePath) {
        log.info("문서 인덱싱 시작 - 파일: {}", filePath.getFileName());

        try {
            String content = Files.readString(filePath);
            Document doc = Document.from(content);
            doc.metadata().put("file_name", filePath.getFileName().toString());
            doc.metadata().put("source", "document");

            // 문서 분할
            DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
            List<TextSegment> segments = splitter.split(doc);

            // 임베딩 생성
            List<Embedding> embeddings = embeddingModel.embedAll(segments)
                    .content();

            // Vector Store에 저장
            documentStore.addAll(embeddings, segments);

            log.info("문서 인덱싱 완료 - 파일: {}, 청크: {}개",
                    filePath.getFileName(), segments.size());

        } catch (Exception e) {
            log.error("문서 인덱싱 실패 - 파일: {}", filePath.getFileName(), e);
        }
    }
}