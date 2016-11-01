package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ssu.standings.service.BaylorExportService;
import org.ssu.standings.service.PropertiesService;
import org.ssu.standings.service.StandingsWatchService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
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


    @RequestMapping(value = "/admin", method = RequestMethod.GET)
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
