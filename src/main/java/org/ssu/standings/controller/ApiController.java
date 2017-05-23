package org.ssu.standings.controller;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.ssu.standings.entity.*;
import org.ssu.standings.entity.contestresponse.*;
import org.ssu.standings.service.*;

import javax.annotation.*;

@Controller
@RequestMapping("/api")
public class ApiController {
    @Resource
    private ApiService apiService;

    @Resource
    private ContestDataStorage contestDataStorage;

    @RequestMapping(value = "/teamlist", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity teamList() throws JsonProcessingException {
        return ResponseEntity.ok(new ObjectMapper()
                .writeValueAsString(apiService.teamList()));
    }

    @RequestMapping(value = "/universitylist", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity univesityList() throws JsonProcessingException {
        return ResponseEntity.ok(new ObjectMapper()
                .writeValueAsString(apiService.universityList()));
    }

    @RequestMapping(value = "/contestlist", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity contestList() throws JsonProcessingException {
        return ResponseEntity.ok(new ObjectMapper()
                .writeValueAsString(apiService.contestList()));
    }

    @RequestMapping(value = "/init-results/{contestId}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getInitResults(@PathVariable Long contestId) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(contestDataStorage.getContestData(contestId));
    }
}
