# GolfRental 프로젝트 - 성능 개선 및 기능 구현 총정리

---

## 📋 전체 작업 요약

| 도메인              | 작업                      | 핵심 기술                         | 효과                  |
|------------------|-------------------------|-------------------------------|---------------------|
| **Chatbot**      | Post Embedding Batch 처리 | LangChain4j, embedAll()       | 36% ↓ (23.8초 → 15초) |
| **Chatbot**      | Redis Vector Store 영속성  | Redis, RediSearch             | 재시작 15초 → 0초        |
| **Reservation**  | 분산 락 동시성 제어             | Redisson                      | Race Condition 방지   |
| **Notification** | 관리자 공지 최적화              | Batch Insert, parallelStream  | 92% ↓ (6.4초 → 0.5초) |
| **Chat**         | 실시간 채팅 시스템              | WebSocket, SSE, Redis Pub/Sub | 멀티 서버 지원            |

---

## 1. Chatbot 성능 개선

### 📊 Issue #4: Post Embedding Batch 처리

**문제점:**

```java
// Before - 개별 호출
for(Post post :posts){
Embedding embedding = embeddingModel.embed(segment).content();  // 1,616번
    postStore.

add(embedding, segment);
}
// 소요 시간: 23.8초
```

**해결:**

```java
// After - Batch 처리
for(int i = 0;
i<total;i +=EMBEDDING_BATCH_SIZE){
List<TextSegment> batchSegments = allSegments.subList(i, end);
List<Embedding> batchEmbeddings = embeddingModel.embedAll(batchSegments).content();
    postStore.

addAll(batchEmbeddings, batchSegments);
}
// 소요 시간: 15초
```

**성과:**

- 네트워크 호출: 1,616번 → 17번 (95% 감소)
- 응답 시간: 23.8초 → 15초 (36% 단축)

---

### 📊 Issue #5: Redis Vector Store 영속성

**문제점:**

```
1번째 실행: 15초 (임베딩 생성)
2번째 실행: 15초 (또 생성) ❌
3번째 실행: 15초 (또 생성) ❌
```

**해결:**

```java
private static final String REDIS_INIT_KEY = "post-embeddings:initialized";

@PostConstruct
public void init() {
    // Redis 플래그 확인
    Boolean isInitialized = redisTemplate.hasKey(REDIS_INIT_KEY);

    if (Boolean.TRUE.equals(isInitialized)) {
        log.info("Post 임베딩 초기화 완료 - Redis 데이터 존재 ({}ms)", duration);
        return;  // 스킵!
    }

    // 임베딩 생성...

    // 완료 후 플래그 저장 (30일 유효)
    redisTemplate.opsForValue().set(REDIS_INIT_KEY, "true", 30, TimeUnit.DAYS);
}
```

**성과:**

- 재시작 시간: 15초 → 0초 (100% 단축)
- 개발 생산성 향상 (재시작 10번 시 238초 → 15초)

**기술 스택:**

- Redis Stack (RediSearch 포함)
- RedisTemplate 플래그 체크
- LangChain4j Redis Vector Store

---

## 2. Reservation 분산 락

### 📊 문제: Race Condition

**시나리오:**

```
유저 A: 12/25~12/27 예약 요청
유저 B: 12/25~12/27 예약 요청 (동시)

→ 둘 다 중복 체크 통과
→ 둘 다 저장 성공
→ 같은 날짜에 2개 예약 💥
```

**멀티 서버 환경에서 애플리케이션 레벨 체크는 무용지물**

---

### ✅ 해결: Redisson 분산 락

```java
String lockKey = "reservation:post:" + request.postId();
RLock lock = redissonClient.getLock(lockKey);

try{
boolean available = lock.tryLock(3, 5, TimeUnit.SECONDS);
    if(!available){
        throw new

ReservationException(LOCK_ACQUISITION_FAILED);
    }

// 락 안에서 검증 + 저장
validateReservationCreation(...);
    reservationRepository.

save(reservation);
    
}finally{
        if(lock.

isHeldByCurrentThread()){
        lock.

unlock();
    }
            }
```

**핵심 설계:**

- Post ID 기준 락: `reservation:post:123`
- 같은 Post만 직렬화, 다른 Post는 동시 실행 가능
- 대기 3초 / 점유 5초 (데드락 방지)

**성과:**

- Race Condition 완전 방지
- 멀티 서버 환경 대응
- 성능 최적화 (Post별 독립적 락)

---

## 3. Notification 성능 개선

### 📊 Issue #9: 관리자 공지 최적화

**Before vs After:**

| 단계          | 응답 시간       | 개선율       | 핵심 기술                 |
|-------------|-------------|-----------|-----------------------|
| **Before**  | **6,444ms** | -         | -                     |
| **Phase 1** | 1,405ms     | 78% ↓     | N+1 제거 + Batch Insert |
| **Phase 2** | **513ms**   | **92% ↓** | **+ parallelStream**  |

**총 단축: 5,931ms (약 6초!)**

---

### 🔍 상세 개선 내용

**Phase 1: N+1 제거 + Batch Insert**

```java
// Before - N+1 문제
for(User user :users){
Notification notification = new Notification(user, ...);
        notificationRepository.

save(notification);  // 1,000번 DB 호출
}

// After - Batch Insert
List<Notification> notifications = users.stream()
        .map(user -> new Notification(user, ...))
        .

toList();
notificationRepository.

saveAll(notifications);  // 1번 DB 호출
```

**효과:**

- JDBC Statements: 2,043개 → 1,022개 (50% 감소)
- Flush: 1,022번 → 1번 (99.9% 감소)
- 응답 시간: 6,444ms → 1,405ms (78% 단축)

---

**Phase 2: 병렬 Redis 발행**

```java
// Before - 순차 발행
notifications.forEach(notification ->
        redisPublisher.

publish(notification)  // 순차 처리 ~1.5초
);

// After - 병렬 발행
AtomicInteger publishCount = new AtomicInteger(0);

notifications.

parallelStream().

forEach(notification ->{
        redisPublisher.

publish(notification);  // 병렬 처리 ~0.1초
    publishCount.

incrementAndGet();
});
```

**효과:**

- Redis 발행: ~1.5초 → ~0.1초 (90% 단축)
- 응답 시간: 1,405ms → 513ms (63% 추가 단축)
- **멀티코어 CPU 활용**

---

### 💡 핵심 교훈

**Flush 횟수의 중요성:**

```
1,022번 save() = 1,022번 flush = 초당 1.5MB 엔티티 처리
→ 네트워크/메모리 오버헤드 심각

1번 saveAll() = 1번 flush = 효율적
```

**병렬 처리 효과:**

```
예상: 1,405ms → 900ms (Redis만 병렬화)
실제: 1,405ms → 513ms (2배 효과!)
→ CPU 멀티코어 활용의 중요성
```

---

## 4. Chat 실시간 채팅 시스템

### 📊 시스템 구조

```
[클라이언트]
    ↓ WebSocket (채팅)
    ↓ SSE (알림)
[서버 A]
    ↓ Redis Pub/Sub
[Redis]
    ↓ Subscribe
[서버 B, C, D...]
```

---

### 🔧 핵심 기술

**1. WebSocket (채팅)**

```java

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // JWT 인증
        // 채팅방 세션 관리
        chatRoomSessions.put(chatRoomId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 메시지 수신
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        // Redis Pub/Sub로 전파
        redisPublisher.publishChatMessage(chatMessage);
    }
}
```

**특징:**

- 양방향 실시간 통신
- JWT 인증 (WebSocket Handshake)
- 채팅방별 세션 관리

---

**2. SSE (알림)**

```java

@GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    Long userId = userPrincipal.getId();
    SseEmitter emitter = new SseEmitter(60 * 1000L);

    // 사용자별 Emitter 관리
    emitters.put(userId, emitter);

    // 하트비트 전송 (연결 유지)
    sendHeartbeat(emitter);

    return emitter;
}
```

**특징:**

- 단방향 (서버 → 클라이언트)
- 자동 재연결
- 하트비트 (30초마다)

---

**3. Redis Pub/Sub (멀티 서버 동기화)**

```java

@Component
public class RedisSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String payload = new String(message.getBody());

        if (channel.equals("chat:message")) {
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

            // 해당 채팅방의 모든 WebSocket 세션에 전송
            sendToWebSocketSessions(chatMessage);
        }
    }
}
```

**구조:**

```
[서버 A] 메시지 수신
    ↓
[Redis] Pub (chat:message)
    ↓
[서버 A, B, C] Subscribe
    ↓
각 서버의 WebSocket 세션으로 전송
```

**효과:**

- 멀티 서버 환경에서 실시간 동기화
- 서버 추가/제거 시 자동 적응
- 단일 실패 지점 없음

---

### 📋 구현된 API

**채팅:**

1. 채팅방 생성: `POST /api/v1/chatrooms`
2. 채팅방 목록: `GET /api/v1/chatrooms`
3. 채팅방 상세: `GET /api/v1/chatrooms/{id}`
4. 메시지 전송: WebSocket `/ws/chat/{chatRoomId}`
5. 읽음 처리: `PATCH /api/v1/chatrooms/{id}/read`

**알림:**

1. SSE 구독: `GET /api/v1/notifications/subscribe`
2. 알림 목록: `GET /api/v1/notifications`
3. 읽음 처리: `PATCH /api/v1/notifications/{id}/read`

---

## 📊 종합 성능 비교

| 항목         | Before         | After | 개선     | 기술                        |
|------------|----------------|-------|--------|---------------------------|
| **챗봇 초기화** | 23.8초          | 15초   | 36% ↓  | embedAll() Batch          |
| **챗봇 재시작** | 15초            | 0초    | 100% ↓ | Redis 플래그                 |
| **알림 공지**  | 6.4초           | 0.5초  | 92% ↓  | Batch + parallelStream    |
| **예약 동시성** | Race Condition | 안전    | ✅      | Redisson 분산 락             |
| **채팅 실시간** | -              | 멀티 서버 | ✅      | WebSocket + Redis Pub/Sub |

---

## 🎯 핵심 교훈

### 1. Batch 처리의 중요성

```
개별 호출: 느림, 네트워크 오버헤드
Batch 호출: 빠름, 효율적
→ 항상 Batch API 먼저 확인
```

**적용 사례:**

- Chatbot: `embedAll()` (1,616번 → 17번)
- Notification: `saveAll()` (1,022번 → 1번)

---

### 2. 영속성 확보

```
매번 재생성: 비효율, 느린 시작
한 번 생성 후 재사용: 빠른 시작
→ 캐싱/플래그 활용
```

**적용 방법:**

- Redis 플래그 체크
- 30일 TTL 자동 만료

---

### 3. 병렬 처리 효과

```
순차 처리: CPU 1개 코어만 사용
병렬 처리: CPU 멀티코어 활용
→ 예상보다 2배 효과!
```

**주의사항:**

- 스레드 안전성 (AtomicInteger)
- 공유 자원 관리

---

### 4. 분산 환경 고려

```
단일 서버: 애플리케이션 락
멀티 서버: 분산 락 필수
→ Redisson, Redis Pub/Sub
```

---

### 5. 측정 가능한 개선

```
"성능 개선했어요" (X)
"36% 성능 향상, 23.8초 → 15초" (O)
```

**로그로 증명:**

```java
long startTime = System.currentTimeMillis();
// ... 작업 ...
long duration = System.currentTimeMillis() - startTime;
log.

info("작업 완료 - {}ms",duration);
```

---

## 🔧 기술 스택 총정리

### Backend

- **Spring Boot 3.3.4**: Java 17
- **JPA/Hibernate**: 엔티티 관리, Batch Insert
- **Redis**: Vector Store, 분산 락, Pub/Sub
- **Redisson**: 분산 락 라이브러리
- **WebSocket**: 실시간 양방향 통신
- **SSE**: 실시간 단방향 통신

### AI/ML

- **LangChain4j**: RAG, Agent, Tool
- **All-MiniLM-L6-v2**: 임베딩 모델 (384 dimension)
- **Gemini 2.0 Flash**: Chat 모델
- **RediSearch**: Vector Search

### Infrastructure

- **MySQL**: 메인 DB
- **Redis Stack**: RediSearch 포함
- **Docker**: 컨테이너화

---

## 📂 변경된 파일 총정리

### Chatbot

1. `build.gradle` - Redis Vector Store 의존성
2. `application-local.yml` - Redis 설정
3. `LangChainConfig.java` - Redis Vector Store Bean
4. `PostEmbeddingService.java` - Batch 처리 + Redis 플래그

### Reservation

1. `build.gradle` - Redisson 의존성
2. `ReservationCommandServiceImpl.java` - 분산 락 적용
3. `ReservationErrorCode.java` - 락 에러 코드

### Notification

1. `NotificationCommandServiceImpl.java` - Batch + parallelStream

### Chat

1. `ChatWebSocketHandler.java` - WebSocket 핸들러
2. `NotificationSseController.java` - SSE 컨트롤러
3. `RedisPublisher.java` - Redis 발행
4. `RedisSubscriber.java` - Redis 구독
5. `ChatController.java` - REST API
6. `ChatRoom.java`, `ChatMessage.java` - 엔티티

---

## 🎓 면접 어필 포인트

### 성능 최적화

```
"챗봇 임베딩 생성을 36% 단축하고, 
관리자 공지를 92% 최적화했습니다."

핵심:
- Batch 처리 (네트워크 호출 95% 감소)
- 병렬 처리 (멀티코어 활용)
- 측정 가능한 개선 (로그 기반 증명)
```

### 분산 시스템

```
"멀티 서버 환경에서 Redis를 활용한 
실시간 통신과 동시성 제어를 구현했습니다."

핵심:
- Redisson 분산 락 (Race Condition 방지)
- Redis Pub/Sub (서버 간 메시지 동기화)
- WebSocket + SSE (실시간 통신)
```

### 문제 해결

```
"N+1 쿼리를 발견하고 Batch Insert로 해결,
Flush 횟수를 99.9% 감소시켰습니다."

핵심:
- Hibernate Statistics 활용
- 숨은 오버헤드 발견
- 측정 → 개선 → 재측정
```

---

## ✅ 최종 체크리스트

### Chatbot

- [x] Issue #4: Batch 처리 (36% 개선)
- [x] Issue #5: Redis 영속성 (100% 개선)
- [x] 성능 측정 및 문서화
- [x] 커밋 메시지 작성

### Reservation

- [x] 분산 락 적용
- [x] Race Condition 방지
- [x] 테스트 가이드 작성

### Notification

- [x] N+1 제거 (78% 개선)
- [x] 병렬 처리 (추가 63% 개선)
- [x] 총 92% 성능 향상
- [x] 문서화 완료

### Chat

- [x] WebSocket 구현
- [x] SSE 구현
- [x] Redis Pub/Sub 연동
- [x] 멀티 서버 동기화

---

## 📅 작성 정보

**작성일**: 2025-12-18  
**작성자**: 윤석호
**프로젝트**: GolfRental (골프 장비 P2P 렌탈)

---

## 🔗 관련 문서

- Chatbot 성능 개선 상세: `챗봇성능개선.md`
- Reservation 분산 락 상세: `예약_분산락.md`
- Notification 성능 개선 상세: `Notification_관리자_공지_성능_개선.md`
- Chat 실시간 통신 상세: `WebSocket_SSE_Redis.md`