package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ssu.standings.entity.ContestInfo;
import org.ssu.standings.entity.Team;
import org.ssu.standings.entity.University;
import org.ssu.standings.service.ApiService;
import org.ssu.standings.service.BaylorExportService;
import org.ssu.standings.service.PropertiesService;
import org.ssu.standings.service.StandingsWatchService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Response;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/")
@Controller
@EnableScheduling
public class SiteController {
    @Resource
    private StandingsWatchService standingsWatchService;
    @Resource
    private BaylorExportService baylorExportService;
    @Resource
    private PropertiesService propertiesService;
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

    @RequestMapping(value = "/admin/deleteteam/{teamId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeTeam(@PathVariable Long teamId) {
        apiService.removeTeam(teamId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/admin/deletecontest/{contestId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeContest(@PathVariable Long contestId) {
        apiService.deleteContest(contestId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/admin/deleteuniversity/{universityId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity removeUniversity(@PathVariable Long universityId) {
        apiService.removeUniversity(universityId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/admin/saveteam", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveTeam(@RequestBody ObjectNode data) throws JsonProcessingException {
        Team team = null;
        try {
            team = new ObjectMapper().readValue(data.get("data").toString(), Team.class);
            team = apiService.saveTeam(team);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(team));
    }

    @RequestMapping(value = "/admin/saveuniversity", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveUniversity(@RequestBody ObjectNode data) throws JsonProcessingException {
        University university = null;
        try {
            university = new ObjectMapper().readValue(data.get("data").toString(), University.class);
            university = apiService.saveUniversity(university);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(university));
    }

    @RequestMapping(value = "/admin/savecontest", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveContest(@RequestBody ObjectNode data) throws JsonProcessingException {
        ContestInfo contestInfo = null;
        try {
            contestInfo = new ObjectMapper().readValue(data.get("data").toString(), ContestInfo.class);
            contestInfo = apiService.saveContest(contestInfo);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(contestInfo));
    }

    @RequestMapping(value = "/login-success", method = RequestMethod.POST)
    public String loginSuccess() {
        return "redirect:/admin";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView adminLoginPage(ModelAndView model) {
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = {"/admin/teams", "/admin", "/admin/universities", "/admin/contests"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView adminHomePage(ModelAndView model) {
        model.setViewName("admin");
        return model;
    }

    private String authorizeCookie(String login, String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (messageDigest != null) {
            messageDigest.update((login + password + login).getBytes());
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            String md5Hex = bigInt.toString(16);

            while (md5Hex.length() < 32) {
                md5Hex = "0" + md5Hex;
            }

            return md5Hex;
        }
        return (login + password + login);
    }

    @RequestMapping("/")
    public ModelAndView homePage(ModelAndView model) {
        model.setViewName("/home");
        return model;
    }

    @RequestMapping(value = "/api/results", method = RequestMethod.GET)
    @ResponseBody
    public String getResults(@RequestParam(value = "last_submit") String lastSubmit) {
        Map<Long, Long> map = new HashMap<>();

        try {
            map = new ObjectMapper().readValue(lastSubmit, new TypeReference<Map<Long, Long>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (standingsWatchService.isContestChanged(map)) {
            try {
                return new ObjectMapper()
                        .writeValueAsString(standingsWatchService.getLastSubmissions(map));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "{}";
    }


    @RequestMapping(value = "/api/init-results", method = RequestMethod.GET)
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


    @RequestMapping(value = "/api/frozen-results", method = RequestMethod.GET)
    @ResponseBody
    public String getFrozenResults(@CookieValue("authorize") String authorizeCookie) {
        if (authorizeCookie(propertiesService.getLogin(), propertiesService.getPassword()).equals(authorizeCookie)) {
            try {
                return new ObjectMapper()
                        .writeValueAsString(standingsWatchService.getFrozenResults());
            } catch (JsonProcessingException e) {

                e.printStackTrace();
            }
        }
        return "{}";
    }

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public ModelAndView authorize(HttpServletResponse response,
                                  @RequestParam(value = "login") String login,
                                  @RequestParam(value = "password") String password) {
        if (login.equals(propertiesService.getLogin()) && password.equals(propertiesService.getPassword())) {
            response.addCookie(new Cookie("authorize", authorizeCookie(propertiesService.getLogin(), propertiesService.getPassword())));
        }

        ModelAndView model = new ModelAndView();
        model.setViewName("/home");
        return model;
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
