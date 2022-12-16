package com.example.sse_sample.rabbit.consumer;

import com.example.sse_sample.document.Notification;
import com.example.sse_sample.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitConsumer {

    private final NotificationService notificationService;

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "#{rabbitConfig.getDynamicQueueName()}")
    public void receiveWN(Notification message){
        notificationService.send(Long.toString(message.getReceiverId()), message.toString());

//        try {
//            Notification notification = objectMapper.readValue(s, Notification.class);
//            notificationService.send(Long.toString(notification.getReceiverId()), s);
//        } catch (JsonProcessingException e) {
//            log.error("json parsing error : ", e);
//        }

    }
}
