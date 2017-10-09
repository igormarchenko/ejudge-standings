package org.ssu.standings.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.ssu.standings.DefaultObjects;
import org.ssu.standings.MockedObjectGenerator;
import org.ssu.standings.entity.BaylorResults;
import org.ssu.standings.entity.ContestDataStorage;
import org.ssu.standings.entity.TeamResultsInBaylor;
import org.ssu.standings.entity.contestresponse.Contest;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BaylorExportServiceTest {
    @InjectMocks
    private BaylorExportService baylorExportService;

    @Mock
    private ContestDataStorage storage;

    @Before
    public void setUp() {
        Contest contest = new Contest.Builder(new DefaultObjects().getContestNode()).withTeamInfo(new MockedObjectGenerator().getTeamList()).build();
        when(storage.getContestData(anyLong())).thenReturn(contest);
    }

    @Test
    public void getBaylorResultsForContest() throws NoSuchFieldException, IllegalAccessException {
        String baylorFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><icpc>\n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"291576\" TotalTime=\"\" TeamName=\"team 1\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"291517\" TotalTime=\"\" TeamName=\"team 2\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"291528\" TotalTime=\"\" TeamName=\"team 3\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"290491\" TotalTime=\"\" TeamName=\"team 4\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"290492\" TotalTime=\"\" TeamName=\"team 5\" /> \n" +
                "</icpc>";
        Long contestId = 1L;

        BaylorResults baylorResults = baylorExportService.getContestResults(contestId, baylorFile);
        Class aClass = BaylorResults.class;
        Field teams = aClass.getDeclaredField("teams");
        teams.setAccessible(true);
        List<TeamResultsInBaylor> results = (List<TeamResultsInBaylor>) teams.get(baylorResults);
        Assert.assertThat(results.get(0).getTeamName(), is("team 4"));
        Assert.assertThat(results.get(1).getTeamName(), is("team 5"));
        Assert.assertThat(results.get(2).getTeamName(), is("team 2"));
        Assert.assertThat(results.get(3).getTeamName(), isOneOf("team 1", "team 3"));
        Assert.assertThat(results.get(4).getTeamName(), isOneOf("team 1", "team 3"));
    }


    @Test
    public void baylorTeamAbsentInEjudgeTest() throws NoSuchFieldException, IllegalAccessException {

        String baylorFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><icpc>\n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"291576\" TotalTime=\"\" TeamName=\"team 1\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"291517\" TotalTime=\"\" TeamName=\"team 2\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"291528\" TotalTime=\"\" TeamName=\"team 3\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"290491\" TotalTime=\"\" TeamName=\"team 4\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"290492\" TotalTime=\"\" TeamName=\"team 5\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"290492\" TotalTime=\"\" TeamName=\"team 6\" /> \n" +
                "</icpc>";
        Long contestId = 1L;

        BaylorResults baylorResults = baylorExportService.getContestResults(contestId, baylorFile);
        Class aClass = BaylorResults.class;
        Field teams = aClass.getDeclaredField("teams");
        teams.setAccessible(true);
        List<TeamResultsInBaylor> results = (List<TeamResultsInBaylor>) teams.get(baylorResults);
        Assert.assertThat(results.size(), is(5));
        Assert.assertThat(results.get(0).getTeamName(), is("team 4"));
        Assert.assertThat(results.get(1).getTeamName(), is("team 5"));
        Assert.assertThat(results.get(2).getTeamName(), is("team 2"));
        Assert.assertThat(results.get(3).getTeamName(), isOneOf("team 1", "team 3"));
        Assert.assertThat(results.get(4).getTeamName(), isOneOf("team 1", "team 3"));
    }

    @Test
    public void ejudgeTeamAbsentInBaylor() throws IllegalAccessException, NoSuchFieldException {
        String baylorFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><icpc>\n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"291517\" TotalTime=\"\" TeamName=\"team 2\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"291528\" TotalTime=\"\" TeamName=\"team 3\" /> \n" +
                "  <Standing LastProblemTime=\"\" ProblemsSolved=\"\" Rank=\"\" TeamID=\"290491\" TotalTime=\"\" TeamName=\"team 4\" /> \n" +
                "</icpc>";
        Long contestId = 1L;

        BaylorResults baylorResults = baylorExportService.getContestResults(contestId, baylorFile);
        Class aClass = BaylorResults.class;
        Field teams = aClass.getDeclaredField("teams");
        teams.setAccessible(true);
        List<TeamResultsInBaylor> results = (List<TeamResultsInBaylor>) teams.get(baylorResults);
        Assert.assertThat(results.size(), is(3));
        Assert.assertThat(results.get(0).getTeamName(), is("team 4"));
        Assert.assertThat(results.get(1).getTeamName(), is("team 2"));
        Assert.assertThat(results.get(2).getTeamName(), is("team 3"));
    }

    @Test
    public void emptyFileTest() throws NoSuchFieldException, IllegalAccessException {
        String baylorFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><icpc></icpc>";
        Long contestId = 1L;

        BaylorResults baylorResults = baylorExportService.getContestResults(contestId, baylorFile);
        Class aClass = BaylorResults.class;
        Field teams = aClass.getDeclaredField("teams");
        teams.setAccessible(true);
        List<TeamResultsInBaylor> results = (List<TeamResultsInBaylor>) teams.get(baylorResults);
        Assert.assertThat(results.size(), is(0));

    }
}