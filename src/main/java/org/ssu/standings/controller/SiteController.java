package org.ssu.standings.controller;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.ssu.standings.entity.*;
import org.ssu.standings.entity.contestresponse.*;
import org.ssu.standings.event.*;
import org.ssu.standings.service.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

@RequestMapping("/")
@Controller
public class SiteController {
    @Resource
    private StandingsWatchService standingsWatchService;
    @Resource
    private BaylorExportService baylorExportService;
    @Resource
    private ApiService apiService;

    @Resource
    private ContestDataStorage contestDataStorage;

    @EventListener
    public void eventListener(ContestUpdates contestUpdates) {
        System.out.println("update!");
    }


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


    @RequestMapping(value = "/api/init-results/{contestId}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getInitResults(@PathVariable Long contestId) throws JsonProcessingException {
        Contest contestData = contestDataStorage.getContestData(contestId);
        return new ObjectMapper().writeValueAsString(contestData);
//        try {
//            return new ObjectMapper()
//                    .writeValueAsString(standingsWatchService.getContestData(contestId));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return "{}";
    }

//    @RequestMapping(value = "/api/results/{contestId}/{time}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
//    @ResponseBody
//    public String getResults(@PathVariable Long contestId, @PathVariable Long time) {
//        try {
//            return new ObjectMapper()
//                    .writeValueAsString(standingsWatchService.getLastSubmissions(contestId, time));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return "{}";
//    }

//    @RequestMapping(value = "/api/frozenresults/{contestId}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
//    @ResponseBody
//    public String getFrozenResults(@PathVariable Long contestId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        try {
//            if(authentication.getAuthorities().stream().filter(item -> "ADMIN".equals(item.getAuthority())).count() > 0) {
//                return new ObjectMapper()
//                        .writeValueAsString(standingsWatchService.getFrozenResults(contestId));
//            } else {
//                return "{}";
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return "{}";
//    }


//    @RequestMapping(value = "/baylor", method = RequestMethod.GET, produces = "application/xml")
//    @ResponseBody
//    public String exportBaylor() throws ParserConfigurationException, JsonProcessingException {
//        return new XmlMapper()
//                .enable(SerializationFeature.CLOSE_CLOSEABLE)
//                .enable(SerializationFeature.INDENT_OUTPUT)
//                .writer()
//                .withRootName("icpc")
//                .writeValueAsString(baylorExportService.getResultsForBaylor())
//                .replaceAll("item", "Standing");
//    }


    @RequestMapping(value = {"/", "/contest/{contestId}"}, produces = "text/plain;charset=UTF-8")
    public ModelAndView homePage(ModelAndView model, @PathVariable Optional<Long> contestId) throws IOException {
        model.setViewName("/home");
        return model;
    }
}
