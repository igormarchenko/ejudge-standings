package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ssu.standings.service.StandingsWatchService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/")
@Controller
@EnableScheduling
public class SiteController {

    @Resource
    private StandingsWatchService standingsWatchService;
    @RequestMapping("/")
    public ModelAndView homePage(ModelAndView model) {
        model.setViewName("home");
        model.addObject("regions", standingsWatchService.getRegionList());
        return model;
    }

    @RequestMapping(value = "/api/results",method = RequestMethod.GET)
    @ResponseBody
    public String getResults(@RequestParam(value = "last_submit") String lastSubmit) {
        Map<String, Long> map = new HashMap<>();

        try {
            map = new ObjectMapper().readValue(lastSubmit, new TypeReference<Map<String, Long>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(standingsWatchService.isContestChanged(map)) {
            try {
                return new ObjectMapper()
                        .writeValueAsString(standingsWatchService.getLastSubmissions(map));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "{}";
    }


    @RequestMapping(value = "/api/init-results",method = RequestMethod.GET)
    @ResponseBody
    public String getResults() {
        try {
            return new ObjectMapper()
                    .writeValueAsString(standingsWatchService.getContestData());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }
}
