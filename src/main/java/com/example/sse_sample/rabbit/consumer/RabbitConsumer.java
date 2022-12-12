package com.example.sse_sample.rabbit.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitConsumer {

    @RabbitListener(queues = "#{rabbitConfig.getDynamicQueueName()}")
    public void receiveTest(Message message){
        System.out.println("message = " + message);
        String s = new String(message.getBody());
        System.out.println("s = " + s);
    }
}
