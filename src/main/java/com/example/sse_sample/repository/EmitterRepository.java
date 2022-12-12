package com.example.sse_sample.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    //sse save
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    //sse finder
    Map<String, SseEmitter> findAllEmitterStartWithById(String memberId);

    //sse delete one
    void deleteById(String id);

    //sse delete many
    void deleteAllEmitterStartWithId(String memberId);

}
