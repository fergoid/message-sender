package com.fergoid.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * Created by markferguson on 21/05/2016.
 */
@MessagingGateway(defaultRequestChannel = "publishSubscribeChannel")
public interface MyGateway {

    @Gateway(requestChannel = "publishSubscribeChannel")
    void publish(String data);
}

