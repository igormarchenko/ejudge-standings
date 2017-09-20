package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.ssu.standings.entity.ContestDataStorage;
import org.ssu.standings.service.ApiService;
import org.ssu.standings.service.BaylorExportService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiController {
    @Resource
    private ApiService apiService;

    @Resource
    private ContestDataStorage contestDataStorage;

    @Resource
    private BaylorExportService baylorExportService;

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

    @RequestMapping(value = "/init-results/{contestId}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String getInitResults(@PathVariable Long contestId) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(contestDataStorage.getContestData(contestId));
    }

    @RequestMapping(value = "/frozen-submits/{contestId}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String getFrozenSubmits(@PathVariable Long contestId) throws JsonProcessingException {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return new ObjectMapper().writeValueAsString(contestDataStorage.getFrozenSubmits(contestId));
    }

    @RequestMapping(value = "/baylor-export/{contestId}", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String exportBaylor(@PathVariable Long contestId, @RequestBody Map<String, String> content) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(
                new XmlMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(baylorExportService.getContestResults(contestId, content.get("content")))
        );

    }
}
