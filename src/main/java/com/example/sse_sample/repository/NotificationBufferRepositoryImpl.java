package com.example.sse_sample.repository;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class NotificationBufferRepositoryImpl implements NotificationBufferRepository {

    /**
     * sse 재 커넥션시 0.5초정도 타임갭이 발생. 해당 갭에 전송받지 못한 데이터를 다시 전송해줄 수 있도록 Buffer 역할을 하는 map
     * map에 데이터 생성 시점부터 3분 후 자동 삭제
     */
    private final Map<String, Object> eventCache = ExpiringMap.builder()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(3, TimeUnit.MINUTES)
            .build();

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithById(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}
