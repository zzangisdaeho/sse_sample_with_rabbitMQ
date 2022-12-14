package com.example.sse_sample.repository;

import com.example.sse_sample.document.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void ttlTest(){
        notificationRepository.save(
                Notification.builder()
                        .content("test")
                        .receiverId("receiverB")
                        .senderId("senderZ")
                        .type(Notification.NotificationType.STRATEGY_PURCHASE)
                        .target("strategyA")
                        .content("I bought3!")
                        .build()
        );
    }

}