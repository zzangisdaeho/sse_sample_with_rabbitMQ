package com.example.sse_sample.repository;

import java.util.Map;

public interface NotificationBufferRepository {

    void saveEventCache(String emitterId, Object event);
    Map<String, Object> findAllEventCacheStartWithById(String memberId);
    void deleteAllEventCacheStartWithId(String memberId);
}
