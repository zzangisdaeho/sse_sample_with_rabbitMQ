package com.example.sse_sample.repository;

import com.example.sse_sample.document.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findAllByUserIdAndCreatedAtAfter(String userId, Date from);
}
