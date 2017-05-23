package org.ssu.standings.controller;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.context.event.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ssu.standings.entity.Message;
import org.ssu.standings.entity.OutputMessage;
import org.ssu.standings.event.*;

import javax.annotation.*;
import java.util.Date;

@Controller
public class WebSocketController {
    @Resource
    private SimpMessagingTemplate template;

    @EventListener
    public void eventListener(ContestUpdates contestUpdates) throws JsonProcessingException {
        template.convertAndSend("/updates/get-updates", new ObjectMapper().writeValueAsString(contestUpdates));
    }

//    @MessageMapping("/listener")
//    @SendTo("/updates/get-updates")
//    public OutputMessage sendMessage(Message message) {
//        System.out.println("in method");
//        return new OutputMessage(message, new Date());
//    }
}
