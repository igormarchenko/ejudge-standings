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

    @RequestMapping(value = {"/", "/contest/{contestId}", "/socket/{contestId}"}, produces = "text/plain;charset=UTF-8")
    public ModelAndView homePage(ModelAndView model, @PathVariable Optional<Long> contestId) throws IOException {
        model.setViewName("/home");
        return model;
    }
}
