package com.example.sse_sample.controller;

import com.example.sse_sample.config.security.annotation.CurrentUser;
import com.example.sse_sample.config.security.entity.User;
import com.example.sse_sample.dto.NotificationDto;
import com.example.sse_sample.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    /**
     * @title 로그인 한 유저 sse 연결
     */
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@CurrentUser User user,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(user.getId(), lastEventId);
    }

    @GetMapping
    public List<NotificationDto> readNotifications(@CurrentUser User user){
        return notificationService.readNotifications(user.getId());
    }

    @PutMapping("/{notificationId}")
    public void updateNotificationReadCondition(@PathVariable String notificationId){
        notificationService.updateNotificationReadCondition(notificationId);
    }

    @PutMapping
    public void updateAllNotificationReadCondition(){
        String userId = null;
        notificationService.updateAllNotificationReadCondition(userId);
    }
}
