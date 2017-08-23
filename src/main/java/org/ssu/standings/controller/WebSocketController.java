package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.ssu.standings.event.ContestUpdates;

import javax.annotation.Resource;

@Controller
public class WebSocketController {
    @Resource
    private SimpMessagingTemplate template;

    @EventListener
    public void eventListener(ContestUpdates contestUpdates) throws JsonProcessingException {
        template.convertAndSend("/updates/get-updates/" + contestUpdates.getContestId(), new ObjectMapper().writeValueAsString(contestUpdates));
    }
}
