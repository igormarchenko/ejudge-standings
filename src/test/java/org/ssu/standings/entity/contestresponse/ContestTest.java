package org.ssu.standings.entity.contestresponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ssu.standings.MockedObjectGenerator;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.ParticipantNode;
import org.ssu.standings.parser.entity.ProblemNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContestTest {
    private Contest contest;

    @Before
    public void setUp() {
        MockedObjectGenerator objectGenerator = new MockedObjectGenerator();
        ContestNode newContest;
        List<ProblemNode> problemNodes;
        List<ParticipantNode> participantNodes;
        List<SubmissionNode> submissionNodes;
        problemNodes = Arrays.asList(
                objectGenerator.defaultProblemNode().withId(1L).withLongName("Test task 1").withShortName("A").build(),
                objectGenerator.defaultProblemNode().withId(2L).withLongName("Test task 2").withShortName("B").build(),
                objectGenerator.defaultProblemNode().withId(3L).withLongName("Test task 3").withShortName("C").build()
        );

        participantNodes = Arrays.asList(
                objectGenerator.defaultParticipantNode().withId(1L).withName("Test team 1").build(),
                objectGenerator.defaultParticipantNode().withId(2L).withName("Test team 2").build(),
                objectGenerator.defaultParticipantNode().withId(3L).withName("Test team 3").build(),
                objectGenerator.defaultParticipantNode().withId(4L).withName("Test team 4").build(),
                objectGenerator.defaultParticipantNode().withId(5L).withName("Test team 5").build()
        );


        submissionNodes = Arrays.asList(
                objectGenerator.defaultSubmissionNode().withId(1L).withProblemId(1L).withRunUuid("1").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(2L).build(),

                objectGenerator.defaultSubmissionNode().withId(2L).withProblemId(1L).withRunUuid("2").withStatus(SubmissionStatus.WA).withTime(60 * 100L).withUserId(3L).build(),

                objectGenerator.defaultSubmissionNode().withId(3L).withProblemId(1L).withRunUuid("3").withStatus(SubmissionStatus.WA).withTime(60 * 20L).withUserId(4L).build(),
                objectGenerator.defaultSubmissionNode().withId(4L).withProblemId(1L).withRunUuid("4").withStatus(SubmissionStatus.OK).withTime(60 * 80L).withUserId(4L).build(),
                objectGenerator.defaultSubmissionNode().withId(5L).withProblemId(2L).withRunUuid("5").withStatus(SubmissionStatus.OK).withTime(60 * 100L).withUserId(4L).build(),

                objectGenerator.defaultSubmissionNode().withId(6L).withProblemId(1L).withRunUuid("6").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(5L).build(),
                objectGenerator.defaultSubmissionNode().withId(7L).withProblemId(2L).withRunUuid("7").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(5L).build()
        );

        newContest = mock(ContestNode.class);
        when(newContest.getContestId()).thenReturn(1L);
        when(newContest.getDuration()).thenReturn(3600L);
        when(newContest.getName()).thenReturn("Test contest");
        when(newContest.getStartTime()).thenReturn(LocalDateTime.of(2017, 3, 25, 3, 57, 10));
        when(newContest.getStopTime()).thenReturn(LocalDateTime.of(2017, 3, 25, 8, 57, 10));
        when(newContest.getCurrentTime()).thenReturn(LocalDateTime.of(2018, 4, 25, 4, 57, 10));
        when(newContest.getFogTime()).thenReturn(100L);
        when(newContest.getUnfogTime()).thenReturn(200L);

        when(newContest.getProblems()).thenReturn(problemNodes);
        when(newContest.getParticipants()).thenReturn(participantNodes);
        when(newContest.getSubmissions()).thenReturn(submissionNodes);

        Map<String, TeamDAO> teams = new HashMap<>();
        teams.put("team 1", objectGenerator.defaultTeamDAO().withId(1L).withName("team 1").build());
        teams.put("team 2", objectGenerator.defaultTeamDAO().withId(2L).withName("team 2").build());
        teams.put("team 3", objectGenerator.defaultTeamDAO().withId(3L).withName("team 3").build());
        teams.put("team 4", objectGenerator.defaultTeamDAO().withId(4L).withName("team 4").build());
        teams.put("team 5", objectGenerator.defaultTeamDAO().withId(5L).withName("team 5").build());
        contest = new Contest.Builder(newContest, teams).build();
    }


    @Test
    public void getResults() throws Exception {
        Assert.assertNotNull(contest.getResults());
        Assert.assertThat(contest.getResults().size(), is(5));

        Assert.assertThat(contest.getResults().get(0).getParticipant().getId(), is(4L));
        Assert.assertThat(contest.getResults().get(1).getParticipant().getId(), is(5L));
        Assert.assertThat(contest.getResults().get(2).getParticipant().getId(), is(2L));
        Assert.assertThat(contest.getResults().get(3).getParticipant().getId(), isOneOf(3L, 1L));
        Assert.assertThat(contest.getResults().get(4).getParticipant().getId(), isOneOf(3L, 1L));
    }

    @Test
    public void getTeamsResults() throws Exception {
        Map<Long, ParticipantResult> results = contest.getTeamsResults(Arrays.asList(1L, 2L, 5L));
        Assert.assertThat(results.size(), is(3));
        Assert.assertThat(results.get(1L).getResults().size(), is(0));
        Assert.assertThat(results.get(2L).getResults().size(), is(1));
        Assert.assertThat(results.get(5L).getResults().size(), is(2));
    }

    @Test
    public void getTeamsResultsOnEmptyList() throws Exception {
        Map<Long, ParticipantResult> results = contest.getTeamsResults(Arrays.asList());
        Assert.assertNotNull(results);
        Assert.assertThat(results.size(), is(0));
    }


    @Test
    public void addSingleSuccessfullSubmission() throws Exception {
        Contest copy = new Contest.Builder(contest).build();

        SubmissionNode submissionNode = new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(1L).withRunUuid("1").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(1L).build();
        copy.updateSubmissions(Arrays.asList(submissionNode));
        Assert.assertThat(copy.getResults().get(3).getParticipant().getId(), is(1L));
    }

    @Test
    public void addSingleWrongSubmission() throws Exception {
        Contest copy = new Contest.Builder(contest).build();

        SubmissionNode submissionNode = new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(2L).withRunUuid("1").withStatus(SubmissionStatus.WA).withTime(60 * 250L).withUserId(1L).build();
        copy.updateSubmissions(Arrays.asList(submissionNode));
        Assert.assertThat(copy.getResults().get(3).getParticipant().getId(), is(1L));
    }


    @Test
    public void addRejudgedSubmission() throws Exception {
        Contest copy = new Contest.Builder(contest).build();

        SubmissionNode submissionNode = new MockedObjectGenerator().defaultSubmissionNode().withId(2L).withProblemId(1L).withRunUuid("2").withStatus(SubmissionStatus.OK).withTime(60 * 100L).withUserId(3L).build();
        copy.updateSubmissions(Arrays.asList(submissionNode));
        Assert.assertThat(copy.getResults().get(2).getParticipant().getId(), is(3L));
    }

    @Test
    public void addSubmissionList() throws Exception {
        List<SubmissionNode> nodes = Arrays.asList(new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(1L).withRunUuid("12").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(3L).build(),
                new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(2L).withRunUuid("13").withStatus(SubmissionStatus.WA).withTime(60 * 250L).withUserId(3L).build(),
                new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(2L).withRunUuid("14").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(3L).build(),
                new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(3L).withRunUuid("15").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(3L).build());

        contest.updateSubmissions(nodes);
        Assert.assertThat(contest.getResults().get(0).getParticipant().getId(), is(3L));
    }

    @Test
    public void getContestId() throws Exception {
        Assert.assertThat(contest.getContestId(), is(1L));
    }

    @Test
    public void getName() throws Exception {
        Assert.assertThat(contest.getName(), is("Test contest"));
    }

    @Test
    public void getDuration() throws Exception {
        Assert.assertThat(contest.getDuration(), is(3600L));
    }

    @Test
    public void getStartTime() throws Exception {
        Assert.assertThat(contest.getStartTime(), is(LocalDateTime.of(2017, 3, 25, 3, 57, 10)));
    }

    @Test
    public void getStopTime() throws Exception {
        Assert.assertThat(contest.getStopTime(), is(LocalDateTime.of(2017, 3, 25, 8, 57, 10)));
    }

    @Test
    public void getCurrentTime() throws Exception {
        Assert.assertThat(contest.getCurrentTime(), is(LocalDateTime.of(2018, 4, 25, 4, 57, 10)));
    }

    @Test
    public void getFogTime() throws Exception {
        Assert.assertThat(contest.getFogTime(), is(100L));
    }

    @Test
    public void getUnfogTime() throws Exception {
        Assert.assertThat(contest.getUnfogTime(), is(200L));
    }

    @Test
    public void getTasks() throws Exception {
        Assert.assertNotNull(contest.getTasks());
        Assert.assertThat(contest.getTasks().size(), is(3));

        Assert.assertThat(contest.getTasks().get(0).getId(), is(1L));
        Assert.assertThat(contest.getTasks().get(1).getId(), is(2L));
        Assert.assertThat(contest.getTasks().get(2).getId(), is(3L));
    }


    @Test
    public void cloneTest() throws NoSuchFieldException, IllegalAccessException {
        Contest copy = new Contest.Builder(contest).build();
        Assert.assertNotSame(contest.getResults(), copy.getResults());
        Class aClass = Contest.class;

        Field field = aClass.getDeclaredField("results");
        field.setAccessible(true);

        Map<Long, ParticipantResult> resultsFromOriginal = (Map<Long, ParticipantResult>) field.get(contest);
        Map<Long, ParticipantResult> resultsFromCopy = (Map<Long, ParticipantResult>) field.get(copy);

        Assert.assertNotSame(resultsFromCopy, resultsFromOriginal);
        Assert.assertNotSame(resultsFromCopy.get(1L), resultsFromOriginal.get(1L));
        Assert.assertNotSame(resultsFromCopy.get(2L), resultsFromOriginal.get(2L));
        Assert.assertNotSame(resultsFromCopy.get(3L), resultsFromOriginal.get(3L));
        Assert.assertNotSame(resultsFromCopy.get(4L), resultsFromOriginal.get(4L));
        Assert.assertNotSame(resultsFromCopy.get(5L), resultsFromOriginal.get(5L));
    }

}