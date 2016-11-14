package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.ssu.standings.service.ApiService;
import org.ssu.standings.service.BaylorExportService;
import org.ssu.standings.service.StandingsWatchService;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;

@RequestMapping("/")
@Controller
@EnableScheduling
public class SiteController {
    @Resource
    private StandingsWatchService standingsWatchService;
    @Resource
    private BaylorExportService baylorExportService;
    @Resource
    private ApiService apiService;

    @RequestMapping(value = "/api/teamlist", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity teamList() throws JsonProcessingException {
        return ResponseEntity.ok(new ObjectMapper()
                .writeValueAsString(apiService.teamList()));
    }

    @RequestMapping(value = "/api/universitylist", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity univesityList() throws JsonProcessingException {
        return ResponseEntity.ok(new ObjectMapper()
                .writeValueAsString(apiService.universityList()));
    }

    @RequestMapping(value = "/api/contestlist", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity contestList() throws JsonProcessingException {
        return ResponseEntity.ok(new ObjectMapper()
                .writeValueAsString(apiService.contestList()));
    }

    @RequestMapping(value = "/login-success", method = RequestMethod.POST)
    public String loginSuccess() {
        return "redirect:/admin/home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView adminLoginPage(ModelAndView model) {
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = "/contest/{contestId}")
    @ResponseBody
    public ModelAndView contestHomePage(@PathVariable Long contestId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("contest");
        return model;
    }

    @RequestMapping("/")
    public ModelAndView homePage(ModelAndView model) {
        model.setViewName("/home");
        return model;
    }

    @RequestMapping(value = "/api/init-results/{contestId}", method = RequestMethod.GET)
    @ResponseBody
    public String getInitResults(@PathVariable Long contestId) {
        try {
            return new ObjectMapper()
                    .writeValueAsString(standingsWatchService.getContestData(contestId));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    @RequestMapping(value = "/api/results/{contestId}/{time}", method = RequestMethod.GET)
    @ResponseBody
    public String getResults(@PathVariable Long contestId, @PathVariable Long time) {
        try {
            return new ObjectMapper()
                    .writeValueAsString(standingsWatchService.getLastSubmissions(contestId, time));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    @RequestMapping(value = "/baylor", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String exportBaylor() throws ParserConfigurationException, JsonProcessingException {
        return new XmlMapper()
                .enable(SerializationFeature.CLOSE_CLOSEABLE)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .writer()
                .withRootName("icpc")
                .writeValueAsString(baylorExportService.getResultsForBaylor())
                .replaceAll("item", "Standing");
    }
}
