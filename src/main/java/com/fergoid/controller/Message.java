package com.fergoid.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by markferguson on 21/05/2016.
 */
public class Message extends ResourceSupport {


    private final String message;

    @JsonCreator
    public Message(@JsonProperty("message") String message) {
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

}
