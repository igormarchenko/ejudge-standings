package org.ssu.standings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.ssu.standings.config.*;
import org.ssu.standings.dao.entity.ContestDAO;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.service.ApiService;
import org.ssu.standings.service.StandingsWatchService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Mock
    private StandingsWatchService watchService;

    @Before
    public void setup() throws PSQLException {
        MockitoAnnotations.initMocks(this);
        doNothing().when(apiService).deleteTeam(anyLong());
        doNothing().when(apiService).deleteUniversity(anyLong());
        doNothing().when(apiService).deleteContest(anyLong());
        doNothing().when(watchService).initContestDataFlow();
        when(apiService.saveTeam(any())).thenReturn(null);
        when(apiService.saveUniversity(any())).thenReturn(null);

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

    private Map<String, TeamDAO> generateteamDAO() {
        UniversityDAO universityDAO = new UniversityDAO.Builder().withId(1L).withType("Technical").withRegion("North").withName("Test university").build();
        TeamDAO teamDAO = new TeamDAO.Builder().withId(1L).withName("Test").withUniversity(universityDAO).build();
        Map<String, TeamDAO> data = new HashMap<>();
        data.put("data", teamDAO);
        return data;
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void saveTeamForAdminUserTest() throws Exception {
        String teamJSON = new ObjectMapper().writeValueAsString(generateteamDAO());
        mockMvc.perform(post("/admin/saveteam").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void saveTeamForObserverUserTest() throws Exception {
        String teamJSON = new ObjectMapper().writeValueAsString(generateteamDAO());
        mockMvc.perform(post("/admin/saveteam").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void saveTeamForUnAuthorizedUserTest() throws Exception {
        String teamJSON = new ObjectMapper().writeValueAsString(generateteamDAO());
        mockMvc.perform(post("/admin/saveteam").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void saveTeamWithoutUniversityTest() throws Exception {
        TeamDAO teamDAO = new TeamDAO.Builder().withId(1L).withName("Test").build();
        Map<String, TeamDAO> data = new HashMap<>();
        data.put("data", teamDAO);
        String teamJSON = new ObjectMapper().writeValueAsString(data);
        mockMvc.perform(post("/admin/saveteam").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void saveUniversityForAdminUser() throws Exception {
        UniversityDAO universityDAO = new UniversityDAO.Builder().withId(1L).withType("Technical").withRegion("North").withName("Test university").build();
        Map<String, UniversityDAO> data = new HashMap<>();
        data.put("data", universityDAO);
        String teamJSON = new ObjectMapper().writeValueAsString(data);
        mockMvc.perform(post("/admin/saveuniversity").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void saveUniversityForObserverUser() throws Exception {
        UniversityDAO universityDAO = new UniversityDAO.Builder().withId(1L).withType("Technical").withRegion("North").withName("Test university").build();
        Map<String, UniversityDAO> data = new HashMap<>();
        data.put("data", universityDAO);
        String teamJSON = new ObjectMapper().writeValueAsString(data);
        mockMvc.perform(post("/admin/saveuniversity").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void saveUniversityForUnauthorizedUser() throws Exception {
        UniversityDAO universityDAO = new UniversityDAO.Builder().withId(1L).withType("Technical").withRegion("North").withName("Test university").build();
        Map<String, UniversityDAO> data = new HashMap<>();
        data.put("data", universityDAO);
        String teamJSON = new ObjectMapper().writeValueAsString(data);
        mockMvc.perform(post("/admin/saveuniversity").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void saveContestForAdminUser() throws Exception {
        ContestDAO contestDAO = new ContestDAO.Builder().withId(1L).withIsFinal(true).withStandingsFiles(new ArrayList<>()).withName("Test").build();
        Map<String, ContestDAO> data = new HashMap<>();
        data.put("data", contestDAO);
        String teamJSON = new ObjectMapper().writeValueAsString(data);
        mockMvc.perform(post("/admin/savecontest").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void saveContestForObserverUser() throws Exception {
        ContestDAO contestDAO = new ContestDAO.Builder().withId(1L).withIsFinal(true).withName("Test").build();
        Map<String, ContestDAO> data = new HashMap<>();
        data.put("data", contestDAO);
        String teamJSON = new ObjectMapper().writeValueAsString(data);
        mockMvc.perform(post("/admin/savecontest").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void saveContestForUnauthorizedUser() throws Exception {
        ContestDAO contestDAO = new ContestDAO.Builder().withId(1L).withIsFinal(true).withName("Test").build();
        Map<String, ContestDAO> data = new HashMap<>();
        data.put("data", contestDAO);
        String teamJSON = new ObjectMapper().writeValueAsString(data);
        mockMvc.perform(post("/admin/savecontest").content(teamJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }
}
