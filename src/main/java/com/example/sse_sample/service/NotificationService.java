package com.example.sse_sample.service;

import com.example.sse_sample.document.Notification;
import com.example.sse_sample.dto.NotificationDto;
import com.example.sse_sample.repository.EmitterRepository;
import com.example.sse_sample.repository.NotificationBufferRepository;
import com.example.sse_sample.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 10; // 10min

    private final EmitterRepository emitterRepository;
    private final NotificationBufferRepository notificationBufferRepository;

    private final NotificationRepository notificationRepository;

    private final ModelMapper modelMapper;

    //notification 구독
    public SseEmitter subscribe(Long userId, String lastEventId) {
        //id 부여
        String id = userId + "_" + System.currentTimeMillis();

        SseEmitter emitter = generateSseEmitter(id);

        emitterRepository.save(id, emitter);

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");

        // 4
        // 클라이언트가 수신되어야 하는 notification list 전송
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = notificationBufferRepository.findAllEventCacheStartWithById(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private SseEmitter generateSseEmitter(String id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        //커넥션이 끊어지면 emitter 삭제하도록 callback
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));
        return emitter;
    }

    // 3
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
        }
    }

    public void send(String id, String content) {
        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithById(id);
        sseEmitters.forEach((key, emitter) -> sendToClient(emitter, key, content));

        // 붙어있는 client가 없다면 buffer에 저장
        if(sseEmitters.isEmpty()){
            String newId = id + "_" + System.currentTimeMillis();
            notificationBufferRepository.saveEventCache(newId, content);
        }
    }

    public List<NotificationDto> readNotifications(String userId){
        List<Notification> recent90DaysNotifications = notificationRepository.findAllByReceiverIdAndCreatedAtAfter(userId, Date.from(ZonedDateTime.now().minusDays(90).toInstant()));

        return recent90DaysNotifications.stream().map(notification -> modelMapper.map(notification, NotificationDto.class)).collect(Collectors.toList());
    }

}
