package com.fergoid.controller;

import com.fergoid.messaging.MyGateway;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by markferguson on 05/05/2016.
 */
@RestController
public class MessageSenderController {

    private final static Logger log = Logger.getLogger("MessageSenderController");

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private TopicExchange exchange;

    @Autowired
    private MyGateway myGateway;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${my.topic}")
    private String topic;

    @Value("${my.exchange}")
    private String exchangeName;

    private Message myMessage;

    // Spring MVC with HATEOAS
    @RequestMapping(path = "/fergoid/send/{message}", method = RequestMethod.PUT)
    public HttpEntity<Message> sendMessage(@PathVariable String message) {
        String s = String.format("I will publish %s to %s", message, exchangeName);
        log.info(s);
        myMessage = null;
        assert(message == null);

        // Publish to {exchangeName} with topic {topic}
        // listener expects bytes[]
        rabbitTemplate.convertAndSend(exchangeName, topic, s.getBytes());
        getMessage(s, "fred", "fred");
        getMessage(s, message, null);
        return new ResponseEntity<>(myMessage, HttpStatus.OK);
    }

    private void getMessage(String displayed,  String message, String relName) {
        //Build Hypermedia Self Link
        if (myMessage == null)
            myMessage = new Message(displayed);
        if (relName == null)
            myMessage.add(linkTo(methodOn(MessageSenderController.class).sendMessage(message)).withSelfRel());
        else
            myMessage.add(linkTo(methodOn(MessageSenderController.class).sendMessage(message)).withRel(relName));

    }


    // Spring Integration
    @RequestMapping(path = "/fergoid/send/integration/{message}", method = RequestMethod.PUT)
    public ResponseEntity<Void> sendMessageOne(@PathVariable String message) {
        String s = String.format("I will publish integration %s", message);
        log.info(s);
        assert(message == null);
        // Publish to {my.other.exchange} with topic {topic}
        // listener expects bytes[]
        myGateway.publish(s);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/fergoid/send/flow/{message}", method = RequestMethod.PUT)
    public ResponseEntity<Void> sendMessageTwo(@PathVariable String message) {
        String s = String.format("I will publish integration %s", message);
        log.info(s);
        assert(message == null);
        // Publish to {my.other.exchange} with topic {topic}
        // listener expects bytes[]
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostConstruct
    public void setUp() {
        log.info("Controller Post Contruction");
        amqpAdmin.declareExchange(exchange);
    }

}
