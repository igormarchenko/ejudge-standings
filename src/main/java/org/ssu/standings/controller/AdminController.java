package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ssu.standings.dao.entity.ContestDAO;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
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
    private StandingsWatchService watchService;

    @RequestMapping(value = {"", "/", "/teams", "/home", "/universities", "/contests"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView adminHomePage(ModelAndView model) {
        model.setViewName("/admin");
        return model;
    }

    @RequestMapping(value = "/deleteteam/{teamId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeTeam(@PathVariable Long teamId) {
        apiService.deleteTeam(teamId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/deletecontest/{contestId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeContest(@PathVariable Long contestId) {
        apiService.deleteContest(contestId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/deleteuniversity/{universityId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeUniversity(@PathVariable Long universityId) {
        apiService.deleteUniversity(universityId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/saveteam", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveTeam(@RequestBody ObjectNode data) throws JsonProcessingException {
        TeamDAO teamDAO;
        try {
            teamDAO = new ObjectMapper()
                    .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                    .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                    .readValue(data.get("data").toString(), TeamDAO.class);
            teamDAO = apiService.saveTeam(teamDAO);
            watchService.initContestDataFlow();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(teamDAO));
    }

    @RequestMapping(value = "/saveuniversity", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveUniversity(@RequestBody ObjectNode data) throws JsonProcessingException {
        UniversityDAO universityDAO;
        try {
            universityDAO = new ObjectMapper().readValue(data.get("data").toString(), UniversityDAO.class);
            universityDAO = apiService.saveUniversity(universityDAO);
            watchService.initContestDataFlow();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(universityDAO));
    }

    @RequestMapping(value = "/savecontest", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveContest(@RequestBody ObjectNode data) throws JsonProcessingException {
        ContestDAO contestDAO;

        try {
            contestDAO = new ObjectMapper().readValue(data.get("data").toString(), ContestDAO.class);
            contestDAO = apiService.saveContest(contestDAO);
            watchService.initContestDataFlow();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(contestDAO));
    }
}
