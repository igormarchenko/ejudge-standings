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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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

    private String authorizeCookie(String login, String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (messageDigest != null) {
            messageDigest.update((login+password+login).getBytes());
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            String md5Hex = bigInt.toString(16);

            while( md5Hex.length() < 32 ){
                md5Hex = "0" + md5Hex;
            }

            return md5Hex;
        }
        return (login+password+login);
    }

    @RequestMapping("/")
    public ModelAndView homePage(ModelAndView model) {
        model.setViewName("home");
//        model.addObject("regions", standingsWatchService.getRegionList());
        return model;
    }

    @RequestMapping(value = "/api/results",method = RequestMethod.GET)
    @ResponseBody
    public String getResults(@RequestParam(value = "last_submit") String lastSubmit) {
        Map<Long, Long> map = new HashMap<>();

        try {
            map = new ObjectMapper().readValue(lastSubmit, new TypeReference<Map<Long, Long>>(){});
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


    @RequestMapping(value = "/api/frozen-results",method = RequestMethod.GET)
    @ResponseBody
    public String getFrozenResults(@CookieValue("authorize") String authorizeCookie) {
        if(authorizeCookie(standingsWatchService.getLogin(), standingsWatchService.getPassword()).equals(authorizeCookie)) {
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
    public ModelAndView authorize(HttpServletResponse response, @RequestParam(value = "login") String login, @RequestParam(value = "password") String password) {
        if(login.equals(standingsWatchService.getLogin()) && password.equals(standingsWatchService.getPassword())) {
            response.addCookie(new Cookie("authorize", authorizeCookie(standingsWatchService.getLogin(), standingsWatchService.getPassword())));
        }

        ModelAndView model = new ModelAndView();
        model.setViewName("home");
//        model.addObject("regions", standingsWatchService.getRegionList());
        return model;
    }
}
