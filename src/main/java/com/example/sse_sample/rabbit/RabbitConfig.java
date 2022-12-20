package com.example.sse_sample.rabbit;

import com.example.sse_sample.document.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Configuration
@Slf4j
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "ns.exchange-fanout.wn.v0";

    @Value("${rabbitmq.dynamic-id}")
    private String dynamicQueueName;

    public String getDynamicQueueName(){
        return this.dynamicQueueName;
    }

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        log.info("queue created : queueName = " + dynamicQueueName);
        return new Queue(dynamicQueueName, false, true, true);
    }

    @Bean
    public Binding binding (Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter(null));
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public MessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper){
//        return new Jackson2JsonMessageConverter(objectMapper);
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
//            SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        configurer.configure(factory, connectionFactory);
//
//        factory.setAfterReceivePostProcessors(message -> {
//            String type = message.getMessageProperties().getHeaders().get("__TypeId__").toString();
//            String typeId = null;
//
//            if (type.equalsIgnoreCase("co.coinvestor.notification_dispatcher.document.Notification")) {
//                typeId = Notification.class.getName();
//            }
//
//            Optional.ofNullable(typeId).ifPresent(t -> message.getMessageProperties().setHeader("__TypeId__", t));
//
//            return message;
//        });
//
//        return factory;
//    }
}
