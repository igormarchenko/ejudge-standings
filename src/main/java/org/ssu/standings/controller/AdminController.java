package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ssu.standings.entity.ContestInfo;
import org.ssu.standings.entity.Team;
import org.ssu.standings.entity.University;
import org.ssu.standings.service.ApiService;
import org.ssu.standings.service.StandingsWatchService;

import javax.annotation.Resource;
import java.io.IOException;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Resource
    private ApiService apiService;
    @Resource
    private StandingsWatchService standingsWatchService;

    @RequestMapping(value = {"/teams", "/home", "/universities", "/contests"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView adminHomePage(ModelAndView model) {
        model.setViewName("admin");
        return model;
    }

    @RequestMapping(value = "/deleteteam/{teamId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeTeam(@PathVariable Long teamId) {
        apiService.removeTeam(teamId);
        standingsWatchService.updateWatchers();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/deletecontest/{contestId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeContest(@PathVariable Long contestId) {
        apiService.deleteContest(contestId);
        standingsWatchService.updateWatchers();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/deleteuniversity/{universityId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeUniversity(@PathVariable Long universityId) {
        apiService.removeUniversity(universityId);
        standingsWatchService.updateWatchers();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/saveteam", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveTeam(@RequestBody ObjectNode data) throws JsonProcessingException {
        Team team = null;
        try {
            team = new ObjectMapper().readValue(data.get("data").toString(), Team.class);
            team = apiService.saveTeam(team);
            standingsWatchService.updateWatchers();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(team));
    }

    @RequestMapping(value = "/saveuniversity", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveUniversity(@RequestBody ObjectNode data) throws JsonProcessingException {
        University university = null;
        try {
            university = new ObjectMapper().readValue(data.get("data").toString(), University.class);
            university = apiService.saveUniversity(university);
            standingsWatchService.updateWatchers();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(university));
    }

    @RequestMapping(value = "/savecontest", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveContest(@RequestBody ObjectNode data) throws JsonProcessingException {
        ContestInfo contestInfo = null;
        try {
            contestInfo = new ObjectMapper().readValue(data.get("data").toString(), ContestInfo.class);
            contestInfo = apiService.saveContest(contestInfo);
            standingsWatchService.updateWatchers();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(contestInfo));
    }

}
