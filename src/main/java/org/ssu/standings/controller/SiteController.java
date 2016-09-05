package org.ssu.standings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class SiteController {
    @RequestMapping("/home")
    public String homePage() {
        return "home";
    }
}
