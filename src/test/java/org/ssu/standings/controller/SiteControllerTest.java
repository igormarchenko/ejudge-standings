package org.ssu.standings.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.ssu.standings.config.CoreConfig;
import org.ssu.standings.config.MvcConfig;
import org.ssu.standings.config.WebInitializer;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MvcConfig.class, CoreConfig.class, WebInitializer.class})
@WebAppConfiguration
public class SiteControllerTest {
    private MockMvc mockMvc;

    @Resource
    private SiteController siteController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(siteController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void loginSuccess() throws Exception {
        mockMvc.perform(post("/login-success"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void adminLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void homePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void contestPage() throws Exception {
        mockMvc.perform(get("/contest/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/contest/sdf"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/contest"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/contest/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void socketPage() throws Exception {
        mockMvc.perform(get("/socket/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/socket/sdf"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/socket"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/socket/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void baylorExportPage() throws Exception {
        mockMvc.perform(get("/baylor-export/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/baylor-export/sdf"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/baylor-export"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/baylor-export/"))
                .andExpect(status().isNotFound());
    }
}