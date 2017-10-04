package org.ssu.standings.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
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
import org.ssu.standings.dao.entity.ContestDAO;
import org.ssu.standings.dao.entity.StandingsFileDAO;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.dao.repository.ContestRepository;
import org.ssu.standings.dao.repository.StandingsFilesRepository;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
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

    @Resource
    private StandingsFilesRepository standingsFilesRepository;

    @Resource
    private ContestRepository contestRepository;

    @Test
    @DatabaseSetup("/database-data.xml")
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
    @DatabaseSetup("/database-data.xml")
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
    @DatabaseSetup("/database-data.xml")
    public void saveTeamWithoutUniversityTest() throws Exception {
        TeamDAO team = new TeamDAO.Builder().withName("test team").build();
        apiService.saveTeam(team);
        List<TeamDAO> teams = apiService.teamList();
        Assert.assertNotNull(teams);
        Assert.assertThat(teams.size(), is(4));

        Assert.assertThat(teams.get(3).getName(), is("test team"));
        Assert.assertNull(teams.get(3).getUniversity());
    }

    @Test
    @DatabaseSetup("/database-data.xml")
    public void saveTeamWithUniversityTest() throws Exception {
        UniversityDAO university = new UniversityDAO.Builder().withId(2L).withType("Test type 2").withRegion("Test region 2").withName("Test uni 2").build();
        TeamDAO team = new TeamDAO.Builder().withName("test team").withUniversity(university).build();
        apiService.saveTeam(team);
        List<TeamDAO> teams = apiService.teamList();
        Assert.assertNotNull(teams);
        Assert.assertThat(teams.size(), is(4));

        Assert.assertThat(teams.get(3).getName(), is("test team"));
        Assert.assertNotNull(teams.get(3).getUniversity());
        Assert.assertThat(teams.get(3).getUniversity().getName(), is(university.getName()));
    }

    @Test
    @DatabaseSetup("/database-data.xml")
    public void deleteTeamTest() throws Exception {
        apiService.deleteTeam(2L);
        List<TeamDAO> teams = apiService.teamList();
        Assert.assertNotNull(teams);
        Assert.assertThat(teams.size(), is(2));

        Assert.assertThat(teams.get(0).getId(), is(1L));
        Assert.assertThat(teams.get(1).getId(), is(3L));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @DatabaseSetup("/database-data.xml")
    public void deleteNonExcitingTeamTest() throws Exception {
        apiService.deleteTeam(4L);
        List<TeamDAO> teams = apiService.teamList();
        Assert.assertNotNull(teams);
        Assert.assertThat(teams.size(), is(3));
    }


    @Test
    @DatabaseSetup("/database-data.xml")
    public void deleteUniversity() throws Exception {
        apiService.deleteUniversity(1L);
        List<UniversityDAO> universities = apiService.universityList();
        Assert.assertNotNull(universities);
        Assert.assertThat(universities.size(), is(1));

        Assert.assertThat(universities.get(0).getId(), is(2L));

        List<TeamDAO> teams = apiService.teamList();
        Assert.assertNull(teams.get(0).getUniversity());
        Assert.assertNull(teams.get(1).getUniversity());
        Assert.assertNotNull(teams.get(2).getUniversity());
    }

    @Test
    @DatabaseSetup("/database-data.xml")
    public void saveUniversity() throws Exception {
        UniversityDAO university = new UniversityDAO.Builder().withId(3L).withType("Test type 3").withRegion("Test region 3").withName("Test uni 3").build();
        apiService.saveUniversity(university);
        List<UniversityDAO> universities = apiService.universityList();
        Assert.assertNotNull(universities);
        Assert.assertThat(universities.size(), is(3));
        Assert.assertThat(universities.get(2).getName(), is(university.getName()));
    }

    @Test
    public void saveNewContestWithoutStandingsFilesTest() throws Exception {
        contestRepository.deleteAll();
        standingsFilesRepository.deleteAll();
        ContestDAO contest = new ContestDAO.Builder().withIsFinal(false).withName("Test contest").build();
        apiService.saveContest(contest);

        Assert.assertNotNull(apiService.contestList());
        Assert.assertThat(apiService.contestList().size(), is(1));

        Assert.assertThat(apiService.contestList().get(0).getFinal(), is(false));
        Assert.assertThat(apiService.contestList().get(0).getName(), is("Test contest"));
    }


    @Test
    public void saveNewContestWithStandingsFilesTest() throws Exception {
        contestRepository.deleteAll();
        standingsFilesRepository.deleteAll();
        ContestDAO contest = new ContestDAO.Builder().withIsFinal(false).withName("Test contest").build();
        StandingsFileDAO standingsFileDAOFirst = new StandingsFileDAO.Builder().withLink("test link 1").withIsFrozen(false).withContest(contest.getId()).build();
        StandingsFileDAO standingsFileDAOSecond = new StandingsFileDAO.Builder().withLink("test link 2").withIsFrozen(true).withContest(contest.getId()).build();
        Class clazz = contest.getClass();
        Field standingsFilesField = clazz.getDeclaredField("standingsFiles");
        standingsFilesField.setAccessible(true);
        standingsFilesField.set(contest, Arrays.asList(standingsFileDAOFirst, standingsFileDAOSecond));

        apiService.saveContest(contest);

        List<ContestDAO> contests = apiService.contestList();

        Assert.assertNotNull(contests);
        Assert.assertThat(contests.size(), is(1));

        Assert.assertThat(contests.get(0).getName(), is("Test contest"));

        Assert.assertNotNull(contests.get(0).getStandingsFiles());
        Assert.assertThat(contests.get(0).getStandingsFiles().size(), is(2));

        Assert.assertThat(contests.get(0).getStandingsFiles().get(0).getLink(), is("test link 1"));
        Assert.assertThat(contests.get(0).getStandingsFiles().get(0).getFrozen(), is(false));

        Assert.assertThat(contests.get(0).getStandingsFiles().get(1).getLink(), is("test link 2"));
        Assert.assertThat(contests.get(0).getStandingsFiles().get(1).getFrozen(), is(true));
    }

    @Test
    @DatabaseSetup("/database-data.xml")
    public void updateExcitingContestWithoutDeletionTest() {
        ContestDAO contest = apiService.contestList().get(0);
        List<StandingsFileDAO> standingsFiles = contest.getStandingsFiles();

        StandingsFileDAO updatedFirstFile = new StandingsFileDAO.Builder(standingsFiles.get(0)).withIsFrozen(false).withLink("updated link 1").build();
        StandingsFileDAO updatedSecondFile = new StandingsFileDAO.Builder(standingsFiles.get(1)).withIsFrozen(true).withLink("updated link 2").build();

        contest = new ContestDAO.Builder(contest).withIsFinal(false).withName("Updated test contest").withStandingsFiles(Arrays.asList(updatedFirstFile, updatedSecondFile)).build();

        apiService.saveContest(contest);
        Assert.assertThat(apiService.contestList().size(), is(1));
        ContestDAO savedContest = apiService.contestList().get(0);
        Assert.assertThat(savedContest.getId(), is(contest.getId()));
        Assert.assertThat(savedContest.getName(), is(contest.getName()));
        List<StandingsFileDAO> files = standingsFilesRepository.findAll();
        Assert.assertThat(files.size(), is(2));

        Assert.assertThat(files.get(0).getLink(), is(updatedFirstFile.getLink()));
        Assert.assertThat(files.get(0).getFrozen(), is(updatedFirstFile.getFrozen()));

        Assert.assertThat(files.get(1).getLink(), is(updatedSecondFile.getLink()));
        Assert.assertThat(files.get(1).getFrozen(), is(updatedSecondFile.getFrozen()));
    }

    @Test
    @DatabaseSetup("/database-data.xml")
    public void updateExcitingContestWithDeletionStandingsFilesTest() {
        ContestDAO contest = apiService.contestList().get(0);
        List<StandingsFileDAO> standingsFiles = contest.getStandingsFiles();

        StandingsFileDAO updatedFirstFile = new StandingsFileDAO.Builder(standingsFiles.get(0)).withIsFrozen(false).withLink("updated link 1").build();

        contest = new ContestDAO.Builder(contest).withIsFinal(false).withName("Updated test contest").withStandingsFiles(Arrays.asList(updatedFirstFile)).build();

        apiService.saveContest(contest);
        Assert.assertThat(apiService.contestList().size(), is(1));
        ContestDAO savedContest = apiService.contestList().get(0);
        Assert.assertThat(savedContest.getId(), is(contest.getId()));
        Assert.assertThat(savedContest.getName(), is(contest.getName()));
        List<StandingsFileDAO> files = standingsFilesRepository.findAll();
        Assert.assertThat(files.size(), is(1));

        Assert.assertThat(files.get(0).getLink(), is(updatedFirstFile.getLink()));
        Assert.assertThat(files.get(0).getFrozen(), is(updatedFirstFile.getFrozen()));

    }

    @Test
    @DatabaseSetup("/database-data.xml")
    public void contestList() throws Exception {
        List<ContestDAO> contests = apiService.contestList();
        Assert.assertNotNull(contests);
        Assert.assertThat(contests.size(), is(1));

        Assert.assertThat(contests.get(0).getFinal(),is(true));
        Assert.assertThat(contests.get(0).getName(),is("Test contest"));
        Assert.assertNotNull(contests.get(0).getStandingsFiles());
        Assert.assertThat(contests.get(0).getStandingsFiles().size(),is(2));

        Assert.assertThat(contests.get(0).getStandingsFiles().get(0).getContestId(),is(1L));
        Assert.assertThat(contests.get(0).getStandingsFiles().get(0).getFrozen(),is(true));
        Assert.assertThat(contests.get(0).getStandingsFiles().get(0).getLink(),is("test link 1"));

        Assert.assertThat(contests.get(0).getStandingsFiles().get(1).getContestId(),is(1L));
        Assert.assertThat(contests.get(0).getStandingsFiles().get(1).getFrozen(),is(false));
        Assert.assertThat(contests.get(0).getStandingsFiles().get(1).getLink(),is("test link 2"));
    }

    @Test
    @DatabaseSetup("/database-data.xml")
    public void deleteContest() throws Exception {

        apiService.deleteContest(1L);

        Assert.assertNotNull(apiService.contestList());
        Assert.assertThat(apiService.contestList().size(), is(0));

        Assert.assertNotNull(standingsFilesRepository.findAll());
        Assert.assertThat(standingsFilesRepository.findAll().size(), is(0));
    }

}