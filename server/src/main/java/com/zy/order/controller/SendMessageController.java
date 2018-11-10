package com.zy.order.controller;

import com.zy.order.message.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SendMessageController {

    @Autowired
    private StreamClient client;

    @GetMapping("/sendMessage")
    public void process(){
        String message = "now is " + new Date();
        client.output().send(MessageBuilder.withPayload(message).build());
    }
}
