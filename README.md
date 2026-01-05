<img src="docs/golfrentalmainimage.png" style="width: 100%; height: 50vh; object-fit: cover; object-position: center 45%;">

# ⛳ GolfRental - 골프 장비 P2P 대여 플랫폼

실시간 채팅, AI 챗봇, 스마트 알림 시스템을 갖춘 현대적인 골프 장비 렌탈 서비스

## 목차

- [1. 팀원 소개](#1-팀원-소개)
- [2. 프로젝트 개요](#2-프로젝트-개요)
- [3. 주요 기술 스택](#3-주요-기술-스택)
- [4. 서비스 플로우](#4-서비스-플로우)
- [5. 아키텍처](#5-아키텍처)
- [6. ERD](#6-erd)
- [7. API 명세](#7-api-명세)
- [8. 기술적 의사결정](#8-기술적-의사결정)
- [9. 트러블 슈팅 & 최적화 전략](#9-트러블-슈팅--최적화-전략)
- [10. 주요 기능](#10-주요-기능)

## 1. 팀원 소개

<div align="center">
  <table>
    <tbody>
      <tr>
        <td align="center" style="padding: 20px;">
          <a href="https://github.com/moominIsCute">
            <img src="https://github.com/moominIsCute.png" width="120px;" alt="윤석호" style="border-radius: 50%;"/>
          </a>
          <div style="margin-top: 10px; font-size: 14px; line-height: 1.2;">
            <b>팀장</b><br />
            <a href="https://github.com/moominIsCute" style="font-size: 16px;">윤석호</a>
            <div style="margin-top: 5px; font-size: 14px;">
              알림<br />채팅<br />챗봇<br />리뷰
            </div>
          </div>
        </td>
        <td align="center" style="padding: 20px;">
          <a href="https://github.com/AllSungho">
            <img src="https://github.com/AllSungho.png" width="120px;" alt="이성호" style="border-radius: 50%;"/>
          </a>
          <div style="margin-top: 10px; font-size: 14px; line-height: 1.2;">
            <b>팀원</b><br />
            <a href="https://github.com/AllSungho" style="font-size: 16px;">이성호</a>
            <div style="margin-top: 5px; font-size: 14px;">
              유저<br />포스트<br />이미지
            </div>
          </div>
        </td>
        <td align="center" style="padding: 20px;">
          <a href="https://github.com/zerone1202">
            <img src="https://github.com/zerone1202.png" width="120px;" alt="박소영" style="border-radius: 50%;"/>
          </a>
          <div style="margin-top: 10px; font-size: 14px; line-height: 1.2;">
            <b>팀원</b><br />
            <a href="https://github.com/zerone1202" style="font-size: 16px;">박소영</a>
            <div style="margin-top: 5px; font-size: 14px;">
              예약<br />결제<br />카테고리
            </div>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</div>

## 2. 프로젝트 개요

**개발 기간:** 2025.12 ~ 2025.12

`GolfRental`은 골프 장비를 빌리고 대여하는 P2P 플랫폼입니다. 사용자 간 직거래를 지원하며, 실시간 채팅, AI 기반 챗봇, 스마트 알림 시스템을 통한 사용자 경험을 제공합니다.
<br>

## 3. 주요 기술 스택

<div align="center">

### **애플리케이션**

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white">

### **인증 및 보안**

<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white">

### **메시징 및 실시간 통신**

<img src="https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socketdotio&logoColor=white"> <img src="https://img.shields.io/badge/SSE-FF6600?style=for-the-badge&logo=serverless&logoColor=white">

### **데이터베이스 & 캐시**

<img src="https://img.shields.io/badge/MySQL-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/Redisson-DC382D?style=for-the-badge&logo=redis&logoColor=white">

### **AI & ML**

<img src="https://img.shields.io/badge/LangChain4j-121212?style=for-the-badge&logo=chainlink&logoColor=white"> <img src="https://img.shields.io/badge/Google%20Gemini-8E75B2?style=for-the-badge&logo=googlegemini&logoColor=white"> <img src="https://img.shields.io/badge/Redis%20Vector-DC382D?style=for-the-badge&logo=redis&logoColor=white">

### **클라우드 & 인프라**

<img src="https://img.shields.io/badge/Amazon%20AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

### **CI/CD & 모니터링**

<img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"> <img src="https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white"> <img src="https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white">

### **결제**

<img src="https://img.shields.io/badge/Toss%20Payments-0064FF?style=for-the-badge&logo=toss&logoColor=white">

### **협업 도구**

![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) <img src="https://img.shields.io/badge/-Swagger-%2385EA2D?style=for-the-badge&logo=swagger&logoColor=white"> ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)

</div>

<br>

## 4. 서비스 플로우

### [골프 장비 대여 프로세스]

1. **물품 등록**
   판매자가 대여할 골프 장비를 등록하고 가격과 대여 기간을 설정합니다.

2. **장비 검색 및 예약**
   사용자는 원하는 골프 장비를 검색하고 예약 신청을 합니다.

3. **실시간 채팅**
   판매자와 구매자는 WebSocket 기반 실시간 채팅으로 소통합니다.

4. **결제 및 확정**
   Toss Payments를 통해 결제하고 예약이 확정됩니다.

5. **리뷰 작성**
   대여 완료 후 리뷰를 작성하여 다른 사용자들에게 정보를 공유합니다.

<br>

### [AI 챗봇 플로우]

```
[사용자 질문]
    ↓
[Embedding Model (All-MiniLM-L6-v2)]
    ↓ 벡터 변환
[Redis Vector Store]
    ↓ 유사도 검색 (Top-5, 코사인 유사도 0.8 이상)
[RAG Context 생성]
    ↓
[Google Gemini 2.0 Flash]
    ↓ Chat Memory 활용 (최근 20개 메시지)
[답변 생성 및 반환]
```

<br>

## 5. 아키텍처

<div align="center">
  <img src="docs/awsArchitecture.png" width="80%">
</div>

### 주요 아키텍처 특징

- **CQRS 패턴**: Command/Query 책임 분리로 유지보수성 향상
- **실시간 통신**: WebSocket과 SSE를 통한 양방향/단방향 통신
- **Redis Pub/Sub**: 멀티 서버 환경에서 메시지 브로드캐스트
- **분산 락**: Redisson을 활용한 동시성 제어
- **AI 통합**: LangChain4j + Google Gemini API
- **벡터 검색**: Redis Vector Store (RediSearch)

<br>

## 6. ERD

<div align="center">
  <img src="docs/golfrentalERD.png" width="80%">
</div>

<br>

## 7. API 명세

자세한 API 문서는 [API 명세서](https://www.notion.so/ERD-API-2b64dacdf82080e9a64ec077a5d792cd?source=copy_link)에서 확인할 수 있습니다.

## 8. 기술적 의사결정

주요 기술 스택 선정 과정과 의사결정 근거를 정리했습니다.

### 주요 의사결정 항목

1. **실시간 통신**: WebSocket + SSE 병행 사용
    - WebSocket: 양방향 채팅 기능
    - SSE: 단방향 알림 기능

2. **AI 챗봇**: LangChain4j + Google Gemini 2.0 Flash
    - Java 네이티브 통합, 빠른 응답 속도, 무료 tier

3. **벡터 DB**: Redis Vector Store (RediSearch)
    - 기존 Redis 인프라 활용, <1ms 응답 속도

4. **분산 락**: Redisson
    - Pub/Sub 방식, 자동 해제, CPU 효율성

5. **메시지 브로커**: Redis Pub/Sub
    - <1ms 지연시간, 기존 인프라 활용

**👉 [상세 내용 보기](docs/기술적의사결정.md)**

<br>

## 9. 트러블 슈팅 & 최적화 전략

프로젝트 개발 과정에서 직면한 주요 문제들과 해결 방법을 정리했습니다.

### 주요 최적화 성과

1. **[성능 최적화] AI 챗봇 임베딩 처리 36% 개선**
    - Batch Embedding 적용: 네트워크 호출 95% 감소
    - Redis 영속성 구현: 재시작 시간 100% 제거

2. **[성능 최적화] 관리자 공지 알림 발송 성능 92% 개선**
    - Batch Insert: JDBC Statements 50% 감소
    - 병렬 Redis 발행: 멀티코어 CPU 활용

3. **[동시성 제어] Redisson 분산 락으로 예약 중복 발생률 0% 달성**
    - Post ID 기준 락으로 Race Condition 완전 차단
    - 멀티 서버 환경에서 안정성 보장

4. **[메모리 최적화] SSE 좀비 연결 제거**
    - 하트비트 메커니즘: 메모리 누수 방지
    - entrySet() 최적화: 조회 성능 개선

5. **[CI/CD 안정화] Redis Vector Store 테스트 환경 구축**
    - Redis Stack 적용으로 CI 테스트 성공률 100% 달성

**👉 [상세 내용 보기](docs/트러블슈팅.md)**

<br>

## 10. 주요 기능

### 1. 실시간 알림 시스템 (SSE)

- Server-Sent Events 기반 실시간 푸시 알림
- 하트비트 메커니즘으로 연결 상태 자동 관리 (30초 주기)
- Redis Pub/Sub을 활용한 멀티 서버 동기화
- `@TransactionalEventListener(AFTER_COMMIT)` + `REQUIRES_NEW`로 트랜잭션 분리

### 2. 실시간 채팅 시스템 (WebSocket)

- WebSocket 기반 양방향 실시간 채팅
- JWT 기반 보안 검증 및 채팅방 권한 관리
- Redis Pub/Sub로 다중 서버 메시지 브로드캐스트
- 로드밸런서 환경에서 Sticky Session 불필요

### 3. RAG 기반 AI 챗봇

- **Redis Vector Store** 활용 벡터 검색
    - RediSearch 기반 코사인 유사도 검색
    - Top-5 결과 반환 (유사도 0.8 이상)
- **LangChain4j** 통합
    - Google Gemini 2.0 Flash API 연동
    - Tool Description 최적화로 LLM 정확도 향상
- **Chat Memory** 구현
    - Redis 기반 대화 맥락 유지 (최근 20개 메시지)
    - 재질문 없이 문맥 기반 답변 가능

### 4. 골프 장비 대여

- 다양한 골프 장비 등록 및 검색
- 카테고리별 필터링 및 정렬
- 이미지 업로드 (AWS S3)
- 상세 정보 및 리뷰 시스템

### 5. 예약 및 결제

- **Redisson 분산 락**으로 중복 예약 방지
- Toss Payments 연동
- 포인트 시스템
- 예약 상태 관리 (대기, 확정, 취소)

### 6. 리뷰 시스템

- 별점 기반 리뷰 작성 (1~5점)
- 이미지 첨부 지원
- 평균 별점 자동 계산
- 리뷰 목록 조회 및 페이징