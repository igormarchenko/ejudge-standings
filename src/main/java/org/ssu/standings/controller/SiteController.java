package org.ssu.standings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

@RequestMapping("/")
@Controller
public class SiteController {
    @RequestMapping(value = "/login-success", method = RequestMethod.POST)
    public String loginSuccess() {
        return "redirect:/";
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
