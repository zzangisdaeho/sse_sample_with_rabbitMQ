package com.example.sse_sample.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "exchange.test.fanout";
    private static final String QUEUE_NAME = "queue.test";
    private static final String ROUTING_KEY = "notification.#";

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
        System.out.println("dynamicQueueName = " + dynamicQueueName);
        return new Queue(dynamicQueueName, false, true, true);
    }

    @Bean
    public Binding binding (Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
