package com.example.sse_sample.repository;

import com.example.sse_sample.document.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void ttlTest(){
        notificationRepository.save(
                Notification.builder()
                        .transactionId(UUID.randomUUID().toString())
                        .payload(Notification.Payload.builder()
                                .content("test")
                                .target("strategyA")
                                .targetUrl("strategyA/URL")
                                .build())
                        .receiverId(1L)
                        .senderInfo(Notification.SenderInfo.builder()
                                .senderId(100L)
                                .senderImgUrl("senderImgUrl")
                                .senderProfileUrl("senderProfileUrl")
                                .build())
                        .type(Notification.NotificationType.STRATEGY_PURCHASE)
                        .source(Notification.NotificationSource.INVESTOR)
                        .build()
        );
    }



}