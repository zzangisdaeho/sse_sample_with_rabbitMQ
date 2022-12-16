package com.example.sse_sample.repository;

import com.example.sse_sample.document.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void ttlTest(){
        notificationRepository.save(
                Notification.builder()
                        .transactionId(UUID.randomUUID().toString())
                        .content("test")
                        .receiverId(1L)
                        .senderInfo(Notification.SenderInfo.builder()
                                .senderId(100L)
                                .senderImgUrl("senderImgUrl")
                                .senderProfileUrl("senderProfileUrl")
                                .build())
                        .type(Notification.NotificationType.STRATEGY_PURCHASE)
                        .target("strategyA")
                        .content("I bought!")
                        .build()
        );
    }

}