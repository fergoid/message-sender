package com.fergoid.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.validation.annotation.Validated;

/**
 * Created by markferguson on 05/05/2016.
 */
@Configuration
@ComponentScan(basePackages = "com.fergoid")
public class MessageSenderConfig {

    @Value("${my.exchange}")
    private String exchange;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    TopicExchange exchange() {
        TopicExchange topex = new TopicExchange(exchange);
        amqpAdmin.declareExchange(topex);
        return topex;
    }

}
