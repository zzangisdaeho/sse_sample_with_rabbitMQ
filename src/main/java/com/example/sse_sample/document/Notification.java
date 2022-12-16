package com.example.sse_sample.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.Date;


@Document(collection = "notifications")
@Builder
@TypeAlias("notification")
@Getter
public class Notification {

    @Id
    private String id;

    private String transactionId;

    //알림을 받을 사용자의 아이디
    @Indexed
    private long receiverId;

    //알림을 보낸 사용자의 아이디
    private SenderInfo senderInfo;

    private NotificationType type;

    //STRATEGY_PURCHASE, STRATEGY_LIKE, NEW_POST : Strategy 식별자
    //POST_REPLY, POST_LIKE, MENTION : POST 식별자
    //FOLLOW : receiverId
    private String target;

    //target으로 바로이동 할 수 있는 url
    private String targetUrl;

    //NEW_POST : 신규 POST 내용
    //POST_REPLY : POST 댓글 내용
    //MENTION : 멘션된 POST의 내용
    private Object content;

    //receiver가 해당 메세지를 봤는지 여부
    private boolean read;


    /**
     *  createdAt 값 기준으로 스타팅하여 expireAfter기준점에 도달시 삭제
     *  mongoDB가 내부적으로 document expire를 체크하는 주기가 있어 최대 1분정도 차이날 수 있음
     *  cf) 기준값 바꾸고싶으면 mongodb에서 index 삭제해주고 재실행해야한다. (자동 업데이트 안됨)
     */
    @Builder.Default
    @Indexed(name = "expireAt", expireAfter = "63d")
    private Date createdAt = Date.from(ZonedDateTime.now().toInstant());


    public enum NotificationType{
        STRATEGY_PURCHASE, STRATEGY_LIKE, NEW_POST, POST_REPLY, POST_LIKE,
        MENTION, FOLLOW;
    }

    @Builder
    public static class SenderInfo{
        private Long senderId;

        private String senderImgUrl;

        private String senderProfileUrl;
    }

    public Notification read() {
        read = true;
        return this;
    }
}
