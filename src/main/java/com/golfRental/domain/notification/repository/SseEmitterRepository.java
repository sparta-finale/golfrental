package com.golfRental.domain.notification.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class SseEmitterRepository {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    public SseEmitter get(Long userId) {
        return emitters.get(userId);
    }

    public void deleteById(Long userId) {
        emitters.remove(userId);
    }

    public Set<Long> getAllUserIds() {
        return emitters.keySet();
    }

    public Set<Map.Entry<Long, SseEmitter>> getAllEntries() {
        return emitters.entrySet();
    }

    public int size() {
        return emitters.size();
    }

}
