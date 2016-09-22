package org.ssu.standings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.ssu.standings.service.BaylorExportService;
import org.ssu.standings.service.StandingsWatchService;
import org.ssu.standings.utils.Settings;

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
        if (authorizeCookie(Settings.getLogin(), Settings.getPassword()).equals(authorizeCookie)) {
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
        if (login.equals(Settings.getLogin()) && password.equals(Settings.getPassword())) {
            response.addCookie(new Cookie("authorize", authorizeCookie(Settings.getLogin(), Settings.getPassword())));
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
