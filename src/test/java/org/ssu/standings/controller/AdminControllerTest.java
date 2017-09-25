package org.ssu.standings.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.ssu.standings.config.*;
import org.ssu.standings.service.ApiService;

import javax.annotation.Resource;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MvcConfig.class, CoreConfig.class, WebInitializer.class, SecurityConfig.class, SpringSecurityInitializer.class})
@WebAppConfiguration
public class AdminControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Resource
    @InjectMocks
    private AdminController adminController;

    @Mock
    private ApiService apiService;

    @Before
    public void setup() throws PSQLException {
        MockitoAnnotations.initMocks(this);
        doNothing().when(apiService).deleteTeam(anyLong());
        doNothing().when(apiService).deleteUniversity(anyLong());
        doNothing().when(apiService).deleteContest(anyLong());

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void adminHomePageForAuthorizedTest() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void userListPageForAuthorizedTest() throws Exception {
        mockMvc.perform(get("/admin/teams"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void contestListPageForAuthorizedTest() throws Exception {
        mockMvc.perform(get("/admin/contests"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void universityListPageForAuthorizedTest() throws Exception {
        mockMvc.perform(get("/admin/universities"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void adminHomePageForObserverTest() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void userListPageForObserverTest() throws Exception {
        mockMvc.perform(get("/admin/teams"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void contestListPageForObserverTest() throws Exception {
        mockMvc.perform(get("/admin/contests"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void universityListPageForObserverTest() throws Exception {
        mockMvc.perform(get("/admin/universities"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithAnonymousUser
    public void adminHomePageForUnAuthorizedTest() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    public void userListPageForUnAuthorizedTest() throws Exception {
        mockMvc.perform(get("/admin/teams"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    public void contestListPageForUnAuthorizedTest() throws Exception {
        mockMvc.perform(get("/admin/contests"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    public void universityListPageForUnAuthorizedTest() throws Exception {
        mockMvc.perform(get("/admin/universities"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    public void removeTeamForUnAuthorizedUserTest() throws Exception {
        mockMvc.perform(get("/admin/deleteteam/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void removeTeamForAdminUserTest() throws Exception {
        mockMvc.perform(get("/admin/deleteteam/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void removeTeamForObserverUserTest() throws Exception {
        mockMvc.perform(get("/admin/deleteteam/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void removeContestForUnAuthorizedUserTest() throws Exception {
        mockMvc.perform(get("/admin/deletecontest/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void removeContestForAdminUserTest() throws Exception {
        mockMvc.perform(get("/admin/deletecontest/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void removeContestForObserverUserTest() throws Exception {
        mockMvc.perform(get("/admin/deletecontest/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void removeUniversityForUnAuthorizedUserTest() throws Exception {
        mockMvc.perform(get("/admin/deleteuniversity/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void removeUniversityForAdminUserTest() throws Exception {
        mockMvc.perform(get("/admin/deleteuniversity/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void removeUniversityForObserverUserTest() throws Exception {
        mockMvc.perform(get("/admin/deleteuniversity/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void saveTeam() throws Exception {
    }

    @Test
    public void saveUniversity() throws Exception {
    }

    @Test
    public void saveContest() throws Exception {
    }

}