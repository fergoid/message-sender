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
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
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
@EnableIntegration


public class MessageSenderConfig {

    @Value("${my.other.exchange}")
    private String otherExchange;

    @Value("${my.exchange}")
    private String exchange;

    @Value("${my.topic}")
    private String topic;

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchange);
    }


    @Bean(name="directChannel")
    DirectChannel directChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "directChannel")
    AmqpOutboundEndpoint amqpOutboundEndpoint(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint amqpOutboundEndpoint = new AmqpOutboundEndpoint(amqpTemplate);
        amqpOutboundEndpoint.setRoutingKey(topic);
        amqpOutboundEndpoint.setExchangeName(otherExchange);
        return amqpOutboundEndpoint;
    }



}
