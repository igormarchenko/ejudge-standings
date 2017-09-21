package org.ssu.standings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.ssu.standings.entity.BaylorResults;
import org.ssu.standings.entity.ContestDataStorage;
import org.ssu.standings.service.BaylorExportService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MvcConfig.class, CoreConfig.class, WebInitializer.class, SecurityConfig.class, SpringSecurityInitializer.class})
@WebAppConfiguration
public class ApiControllerTest {
    private MockMvc mockMvc;
    //    @Autowired
//    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private WebApplicationContext context;

    @Resource
    @InjectMocks
    private ApiController apiController;

    @Mock
    private BaylorExportService baylorExportService;

    @Mock
    private ContestDataStorage storage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(storage.getFrozenSubmits(anyLong())).thenReturn(new ArrayList<>());
        when(baylorExportService.getContestResults(anyLong(), any())).thenReturn(new BaylorResults(new ArrayList<>()));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .standaloneSetup(apiController)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void teamList() throws Exception {
        mockMvc.perform(get("/api/teamlist"))
                .andExpect(status().isOk());
    }

    @Test
    public void univesityList() throws Exception {
        mockMvc.perform(get("/api/universitylist"))
                .andExpect(status().isOk());
    }

    @Test
    public void contestList() throws Exception {
        mockMvc.perform(get("/api/contestlist"))
                .andExpect(status().isOk());
    }

    @Test
    public void getInitResults() throws Exception {
        mockMvc.perform(get("/api/init-results/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/init-results/sdf"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void getFrozenSubmitsWithAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/frozen-submits/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void getFrozenSubmitsWithObserver() throws Exception {
        mockMvc.perform(get("/api/frozen-submits/1"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void getFrozenSubmitsWithAdmin() throws Exception {
        mockMvc.perform(get("/api/frozen-submits/1"))
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    public void exportBaylorFromUnauthorized() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("content", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><icpc></icpc>");
        String json = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(post("/api/baylor-export/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void exportBaylorFromAdmin() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("content", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><icpc></icpc>");
        String json = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(post("/api/baylor-export/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "observer", authorities = {"OBSERVER"})
    public void exportBaylorFromObserver() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("content", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><icpc></icpc>");
        String json = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(post("/api/baylor-export/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }
}