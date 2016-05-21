package com.fergoid.controller;

import com.fergoid.messaging.MyGateway;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

/**
 * Created by markferguson on 05/05/2016.
 */
@RestController
public class MessageSenderController {

    private final static Logger log = Logger.getLogger("MessageSenderController");

    @Autowired
    MyGateway myGateway;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${my.topic}")
    private String topic;

    @Value("${my.exchange}")
    private String exchangeName;


    @RequestMapping(path = "/fergoid/send/{message}", method = RequestMethod.PUT)
    public ResponseEntity<Void> sendMessage(@PathVariable String message) {
        log.info("I will publish " + message + " to " +exchangeName);
        assert(message == null);
        // Publish to {exchangeName} with topic {topic}
        // listener exepects bytes[]
        rabbitTemplate.convertAndSend(exchangeName, topic, message.getBytes());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(path = "/fergoid/send/integration/{message}", method = RequestMethod.PUT)
    public ResponseEntity<Void> sendMessageOne(@PathVariable String message) {
        String s = "I will publish integration " + message;
        log.info(s);
        assert(message == null);
        // Publish to {my.other.exchange} with topic {topic}
        // listener exepects bytes[]
        myGateway.publish(s);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
