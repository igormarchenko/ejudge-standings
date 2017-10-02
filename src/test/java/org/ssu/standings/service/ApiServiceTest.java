package org.ssu.standings.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.ssu.standings.config.CoreConfig;
import org.ssu.standings.config.MvcConfig;
import org.ssu.standings.config.PersistenceConfig;
import org.ssu.standings.config.WebInitializer;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, PersistenceConfig.class, WebInitializer.class, MvcConfig.class})
@WebAppConfiguration

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@ActiveProfiles("test")
public class ApiServiceTest{

    @Resource
    private ApiService apiService;
    @Test
    public void teamListFromEmptyDatabaseTest() throws Exception {
        List<TeamDAO> teams = apiService.teamList();
        Assert.assertNotNull(teams);
        Assert.assertThat(teams.size(), is(0));
    }

    @Test
    @DatabaseSetup("/team-data.xml")
    public void teamListTest() throws Exception {
        List<TeamDAO> teams = apiService.teamList();
        Assert.assertNotNull(teams);
        Assert.assertThat(teams.size(), is(3));

        Assert.assertThat(teams.get(0).getId(), is(1L));
        Assert.assertThat(teams.get(0).getName(), is("Test team 1"));
        Assert.assertThat(teams.get(0).getUniversity().getId(), is(1L));
        Assert.assertThat(teams.get(0).getUniversity().getRegion(), is("Test region 1"));
        Assert.assertThat(teams.get(0).getUniversity().getName(), is("Test uni 1"));
        Assert.assertThat(teams.get(0).getUniversity().getType(), is("Test type 1"));

        Assert.assertThat(teams.get(2).getId(), is(3L));
        Assert.assertThat(teams.get(2).getName(), is("Test team 3"));
        Assert.assertThat(teams.get(2).getUniversity().getId(), is(2L));
        Assert.assertThat(teams.get(2).getUniversity().getRegion(), is("Test region 2"));
        Assert.assertThat(teams.get(2).getUniversity().getName(), is("Test uni 2"));
        Assert.assertThat(teams.get(2).getUniversity().getType(), is("Test type 2"));
    }

    @Test
    public void universityListFromEmptyDatabaseTest() throws Exception {
        List<UniversityDAO> universities = apiService.universityList();

        Assert.assertNotNull(universities);
        Assert.assertThat(universities.size(), is(0));
    }

    @Test
    @DatabaseSetup("/team-data.xml")
    public void universityListTest() throws Exception {
        List<UniversityDAO> universities = apiService.universityList();

        Assert.assertNotNull(universities);
        Assert.assertThat(universities.size(), is(2));
        Assert.assertThat(universities.get(0).getId(), is(1L));
        Assert.assertThat(universities.get(0).getRegion(), is("Test region 1"));
        Assert.assertThat(universities.get(0).getName(), is("Test uni 1"));
        Assert.assertThat(universities.get(0).getType(), is("Test type 1"));

        Assert.assertThat(universities.get(1).getId(), is(2L));
        Assert.assertThat(universities.get(1).getRegion(), is("Test region 2"));
        Assert.assertThat(universities.get(1).getName(), is("Test uni 2"));
        Assert.assertThat(universities.get(1).getType(), is("Test type 2"));
    }

    @Test
    public void saveTeam() throws Exception {
    }

    @Test
    public void deleteTeam() throws Exception {
    }

    @Test
    public void deleteUniversity() throws Exception {
    }

    @Test
    public void saveUniversity() throws Exception {
    }

    @Test
    public void saveContest() throws Exception {
    }

    @Test
    public void contestList() throws Exception {
    }

    @Test
    public void deleteContest() throws Exception {
    }

}