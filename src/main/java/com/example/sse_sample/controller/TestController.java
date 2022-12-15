package com.example.sse_sample.controller;

import com.example.sse_sample.dto.NotificationDto;
import com.example.sse_sample.rabbit.RabbitConfig;
import com.example.sse_sample.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final NotificationService notificationService;

    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/hello")
    public String hello(){
        return "HELLO";
    }

    @PostMapping("/rabbitmq")
    public String createNotification(long id, String content){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, "notification", new NotificationDto(content, id));
        return content;
    }

    @PostMapping("/notification/send")
    public void sendNotification(String id, String content){
        notificationService.send(id, content);
    }

    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(id, lastEventId);
    }

    @GetMapping
    public List<NotificationDto> readNotifications(@RequestParam long userId){
        return notificationService.readNotifications(userId);
    }

    @PatchMapping("/{notificationId}")
    public void updateNotificationReadCondition(@PathVariable String notificationId){
        notificationService.updateNotificationReadCondition(1L, notificationId);
    }

    @PatchMapping
    public void updateAllNotificationReadCondition(@RequestParam long userId){
        notificationService.updateAllNotificationReadCondition(userId);
    }

}
