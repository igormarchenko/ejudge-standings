package org.ssu.standings.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ssu.standings.entity.Message;
import org.ssu.standings.entity.OutputMessage;

import java.util.Date;

@RequestMapping("/ws")
@Controller
public class WebSocketController {

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public OutputMessage sendMessage(Message message) {
        return new OutputMessage(message, new Date());
    }
}
