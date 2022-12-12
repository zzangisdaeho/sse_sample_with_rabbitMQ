package com.example.sse_sample.controller;

import com.example.sse_sample.dto.NotificationDto;
import com.example.sse_sample.rabbit.RabbitConfig;
import com.example.sse_sample.repository.NotificationBufferRepository;
import com.example.sse_sample.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final NotificationService notificationService;

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/rabbitmq")
    public String createNotification(String id, String content){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, "notification", new NotificationDto(content, id));
        return content;
    }

    @PostMapping("/notification/send")
    public void sendNotification(String id, String content){
        notificationService.send(id, content);
    }
}
