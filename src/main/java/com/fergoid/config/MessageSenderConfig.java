package com.fergoid.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
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
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by markferguson on 05/05/2016.
 */
@Configuration
@ComponentScan(basePackages = "com.fergoid")
@IntegrationComponentScan(basePackages = "com.fergoid")

public class MessageSenderConfig {

    @Value("${my.other.exchange}")
    private String exchange;

    @Value("${my.topic}")
    private String topic;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    TopicExchange exchange() {
        TopicExchange topex = new TopicExchange(exchange);
        amqpAdmin.declareExchange(topex);
        return topex;
    }


    @Bean(name="publishSubscribeChannel")
    PublishSubscribeChannel publishSubscribeChannel() {
        PublishSubscribeChannel psc = new PublishSubscribeChannel( threadPoolTaskExecutor());
        psc.setMinSubscribers(0);
        return psc;
    }

    @Bean
    ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor ioExec = new ThreadPoolTaskExecutor();
        ioExec.setCorePoolSize(4);
        ioExec.setMaxPoolSize(10);
        ioExec.setQueueCapacity(0);
        ioExec.setThreadNamePrefix("io-");
        ioExec.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        ioExec.initialize();
        return ioExec;
    }

    @Bean
    @ServiceActivator(inputChannel = "publishSubscribeChannel")
    AmqpOutboundEndpoint amqpOutboundEndpoint(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint amqpOutboundEndpoint = new AmqpOutboundEndpoint(amqpTemplate);
        amqpOutboundEndpoint.setRoutingKey(topic);
        amqpOutboundEndpoint.setExchangeName(exchange);
        return amqpOutboundEndpoint;
    }



}
