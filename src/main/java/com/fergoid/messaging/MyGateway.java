package com.fergoid.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * Created by markferguson on 21/05/2016.
 */
@MessagingGateway(defaultRequestChannel = "directChannel")
public interface MyGateway {

    @Gateway(requestChannel = "directChannel")
    void publish(String data);
}

