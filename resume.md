# GolfRental 프로젝트 | 2024.11 ~ 2024.12 | 팀 프로젝트 / 백엔드 개발

## 프로젝트 개요

- 골프 장비 대여 플랫폼
- 실시간 알림/채팅 시스템 및 AI 챗봇 구축 담당
- Java, Spring Boot, Redis, WebSocket, SSE, LangChain4j 활용

---

## 핵심 구현

### 1. 실시간 알림 시스템 (SSE 기반)

- **SSE(Server-Sent Events)** 기반 실시간 푸시 알림 구현
- 단방향 푸시 특성에 적합, HTTP/2 호환, 구현 복잡도 낮음
- **하트비트(Heartbeat) 메커니즘** 구현
- 30초 주기로 연결 상태 확인, 좀비 연결 자동 정리
- entrySet 기반 루프로 최적화 → 반복적인 Repository 조회 제거
- **Redis Pub/Sub** 활용한 멀티 서버 동기화
- 지연시간 <1ms, 서버 간 실시간 이벤트 전파
- **트랜잭션 분리 설계**
- `@TransactionalEventListener(AFTER_COMMIT)` + `REQUIRES_NEW`
- 알림 실패가 비즈니스 로직에 영향 없도록 독립 처리

### 2. 실시간 채팅 시스템 (WebSocket 기반)

- **WebSocket** 기반 양방향 실시간 채팅 구현
- 저지연 요구사항 충족, 채팅방별 세션 관리
- **JWT 기반 보안 검증**
- `JwtHandshakeInterceptor`로 WebSocket 연결 시 토큰 검증
- 채팅방 참여자 권한 검증
- **Redis Pub/Sub**로 다중 서버 메시지 브로드캐스트
- 로드밸런서 환경에서 Sticky Session 불필요
- 모든 서버 인스턴스 간 메시지 동기화

### 3. RAG 기반 AI 챗봇 시스템

- **Redis Vector Store** 적용
- RediSearch 활용 벡터 검색 구현
- 코사인 유사도 기반 Top-5 결과 반환 (유사도 0.8 이상)
- 기존 Redis 인프라 활용으로 추가 비용 없음
- **LangChain4j** 통합
- Google Gemini API 연동
- Tool Description 최적화로 LLM 도구 호출 정확도 향상
- 임베딩 배치 처리 로직 개선
- **Chat Memory** 구현
- Redis 기반 대화 맥락 유지 (최근 20개 메시지)
- 재질문 없이 문맥 기반 답변 가능

### 4. 동시성 제어 (Redisson 분산 락)

- **Redisson 분산 락**으로 중복 예약 방지
- Lock 대기 시간: 3초, 자동 해제: 5초
- Pub/Sub 방식으로 폴링 대비 CPU 효율성 향상
- 서버 크래시 시 데드락 방지 (Lease Time 자동 해제)
- **멀티 서버 환경** 동시성 제어
- DB Lock 방식 대비 DB 부하 최소화
- Race Condition 완전 제거

### 5. 관리자 공지 브로드캐스트

- 모든 접속 중인 사용자에게 실시간 공지 전송
- 배치 처리 (1000명 단위)로 대량 발송 최적화
- 오프라인 사용자는 DB 저장 후 추후 조회 가능

---

## Trouble Shooting

### 1. 하트비트 루프 성능 개선

- **문제**: `keySet()` 순회 후 `get(userId)` 호출로 N번의 해시 조회 발생
- **해결**: `entrySet()` 기반 루프로 변경
- **성과**: 불필요한 Repository 조회 제거, Null Check 로직 간소화

### 2. 분산 환경 중복 예약 방지

- **문제**: 동일 장비에 대한 동시 예약 요청 시 중복 발생
- **해결**: Redisson 분산 락 적용, Lock 획득 후 중복 검증
- **성과**: 중복 예약 발생률 0%, 멀티 서버 환경 안정성 확보

### 3. 좀비 연결(Zombie Connection) 정리

- **문제**: SSE 연결이 끊긴 클라이언트가 메모리에 남아있음
- **해결**: 30초 주기 하트비트로 연결 상태 확인, 실패 시 자동 제거
- **성과**: 메모리 누수 방지, 타임아웃(6분) 전 연결 유지

### 4. Redis Vector Store CI 환경 이슈

- **문제**: 기본 Redis 이미지에서 RediSearch 모듈 미지원
- **해결**: `redis-stack-server` 이미지로 변경
- **성과**: CI/CD 파이프라인 정상화, 벡터 검색 테스트 통과

---

## 사용 기술

- **Backend**: Java, Spring Boot, Spring Security
- **Real-time**: WebSocket, SSE (Server-Sent Events)
- **Cache/Message Broker**: Redis, Redis Pub/Sub, Redisson
- **AI/ML**
- LangChain4j, Google Gemini API, Redis Vector Store (RediSearch)
  • ​Infra​: Docker, GitHub Actions (CI/CD)