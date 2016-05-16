package com.fergoid.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by markferguson on 05/05/2016.
 */
@RestController
public class MessageSenderController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${my.topic}")
    private String topic;

    @Value("${my.exchange}")
    private String exchangeName;

    @RequestMapping(path = "/fergoid/send/{message}", method = RequestMethod.PUT)
    public ResponseEntity<Void> sendMessage(@PathVariable String message) {
        System.out.println("I will publish " + message);
        assert(message == null);
        // Publish to {exchangeName} with topic {topic}
        // listener exepects bytes[]
        rabbitTemplate.convertAndSend(exchangeName, topic, message.getBytes());
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
