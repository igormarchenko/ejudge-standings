package org.ssu.standings.entity;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.ssu.standings.DefaultObjects;
import org.ssu.standings.MockedObjectGenerator;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.ParticipantResult;
import org.ssu.standings.event.ContestUpdatesEventProducer;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.ParticipantNode;
import org.ssu.standings.parser.entity.ProblemNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ContestDataStorageTest {
    private ContestNode getContestNode() {
        return new DefaultObjects().getContestNode();
    }

    private MockedObjectGenerator.MockedContestNodeBuilder getDefaultMockedContestBuilder() {
        return new DefaultObjects().getDefaultMockedContestBuilder();
    }

    @Test
    public void addNewContestTest() throws NoSuchFieldException, IllegalAccessException {

        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        Assert.assertNull(storage.getContestData(contest.getContestId()));
        storage.updateContest(contest.getContestId(), contest, true);
        Assert.assertNotNull(storage.getContestData(contest.getContestId()));

        Class aClass = ContestDataStorage.class;

        Field contestInStorageField = aClass.getDeclaredField("contestData");
        contestInStorageField.setAccessible(true);
        Map<Long, Contest> contestsInStorage = (Map<Long, Contest>) contestInStorageField.get(storage);
        Assert.assertNotNull(contestsInStorage);
        Assert.assertThat(contestsInStorage.size(), is(1));
        Assert.assertThat(contestsInStorage.get(contest.getContestId()).getContestId(), is(contest.getContestId()));

        Field isContestFrozenField = aClass.getDeclaredField("isContestFrozen");
        isContestFrozenField.setAccessible(true);
        Map<Long, Boolean> isContestFrozen = (Map<Long, Boolean>) isContestFrozenField.get(storage);
        Assert.assertNotNull(isContestFrozen);
        Assert.assertThat(isContestFrozen.size(), is(1));
        Assert.assertThat(isContestFrozen.get(contest.getContestId()), is(true));
    }

    @Test
    public void setContestFrozenStatusTest() throws NoSuchFieldException, IllegalAccessException {
        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        Class aClass = ContestDataStorage.class;

        Field contestInStorageField = aClass.getDeclaredField("contestData");
        contestInStorageField.setAccessible(true);
        Field isContestFrozenField = aClass.getDeclaredField("isContestFrozen");
        isContestFrozenField.setAccessible(true);

        Assert.assertNull(storage.getContestData(contest.getContestId()));
        storage.updateContest(contest.getContestId(), contest, true);
        storage.updateContest(contest.getContestId(), contest, false);
        Assert.assertNotNull(storage.getContestData(contest.getContestId()));

        Map<Long, Contest> contestsInStorage = (Map<Long, Contest>) contestInStorageField.get(storage);
        Assert.assertNotNull(contestsInStorage);
        Assert.assertThat(contestsInStorage.size(), is(1));
        Assert.assertThat(contestsInStorage.get(contest.getContestId()).getContestId(), is(contest.getContestId()));

        Map<Long, Boolean> isContestFrozen = (Map<Long, Boolean>) isContestFrozenField.get(storage);
        Assert.assertNotNull(isContestFrozen);
        Assert.assertThat(isContestFrozen.size(), is(1));
        Assert.assertThat(isContestFrozen.get(contest.getContestId()), is(false));
    }

    @Test
    public void updateContestNameTest() {
        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, true);
        ContestNode updatedContest = getDefaultMockedContestBuilder().withName("Test contest updated title").build();
        storage.updateContest(contest.getContestId(), updatedContest, true);
        Assert.assertThat(storage.getContestData(contest.getContestId()).getName(), is("Test contest updated title"));
    }

    @Test
    public void updateContestFogTimeTest() {
        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, true);
        ContestNode updatedContest = getDefaultMockedContestBuilder().withFogTime(110L).build();
        storage.updateContest(contest.getContestId(), updatedContest, true);

        Assert.assertThat(storage.getContestData(contest.getContestId()).getFogTime(), is(110L));
    }

    @Test
    public void updateContestDurationTest() {
        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, true);
        ContestNode updatedContest = getDefaultMockedContestBuilder().withDuration(1100L).build();
        storage.updateContest(contest.getContestId(), updatedContest, true);

        Assert.assertThat(storage.getContestData(contest.getContestId()).getDuration(), is(1100L));
    }

    @Test
    public void updateContestUnfogTimeTest() {
        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, true);
        ContestNode updatedContest = getDefaultMockedContestBuilder().withUnfogTime(310L).build();
        storage.updateContest(contest.getContestId(), updatedContest, true);

        Assert.assertThat(storage.getContestData(contest.getContestId()).getUnfogTime(), is(310L));
    }


    @Test
    public void updateContestCurrentTimeTest() {
        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, true);
        ContestNode updatedContest = getDefaultMockedContestBuilder().withCurrentTime(LocalDateTime.of(2013, 2, 15, 4, 57, 10)).build();
        storage.updateContest(contest.getContestId(), updatedContest, true);

        Assert.assertThat(storage.getContestData(contest.getContestId()).getCurrentTime(), is(LocalDateTime.of(2013, 2, 15, 4, 57, 10)));
    }

    @Test
    public void updateContestStartTimeTest() {
        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, true);
        ContestNode updatedContest = getDefaultMockedContestBuilder().withStartTime(LocalDateTime.of(2017, 3, 25, 2, 57, 10)).build();
        storage.updateContest(contest.getContestId(), updatedContest, true);

        Assert.assertThat(storage.getContestData(contest.getContestId()).getStartTime(), is(LocalDateTime.of(2017, 3, 25, 2, 57, 10)));
    }

    @Test
    public void updateContestStopTimeTest() {
        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, true);
        ContestNode updatedContest = getDefaultMockedContestBuilder().withStopTime(LocalDateTime.of(2017, 3, 25, 12, 57, 10)).build();
        storage.updateContest(contest.getContestId(), updatedContest, true);

        Assert.assertThat(storage.getContestData(contest.getContestId()).getStopTime(), is(LocalDateTime.of(2017, 3, 25, 12, 57, 10)));
    }

    @Test
    public void rejudgeSubmissionsTest() throws NoSuchFieldException, IllegalAccessException {
        ContestUpdatesEventProducer eventHandler = mock(ContestUpdatesEventProducer.class);
        doNothing().when(eventHandler).publishEvent(any());

        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        Class aClass = ContestDataStorage.class;
        Field field = aClass.getDeclaredField("contestUpdatesEventProducer");
        field.setAccessible(true);
        field.set(storage, eventHandler);

        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, false);

        List<SubmissionNode> updatedSubmissions = new ArrayList<>(new DefaultObjects().getSubmissionNodes());
        updatedSubmissions.add(new MockedObjectGenerator().defaultSubmissionNode().withId(2L).withProblemId(1L).withRunUuid("2").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(3L).build());
        ContestNode updatedContest = getDefaultMockedContestBuilder().withSubmissions(updatedSubmissions).build();
        Assert.assertThat(storage.getContestData(contest.getContestId()).getTeamsResults(Arrays.asList(3L)).get(3L).getResults().get(1L).getStatus(), is(SubmissionStatus.WA));

        storage.updateContest(contest.getContestId(), updatedContest, false);
        Contest contestData = storage.getContestData(contest.getContestId());
        Map<Long, ParticipantResult> teamResults = contestData.getTeamsResults(Arrays.asList(3L));
        Assert.assertThat(teamResults.get(3L).solvedProblems(), is(1));

        Assert.assertThat(teamResults.get(3L).getResults().get(1L).getStatus(), is(SubmissionStatus.OK));
        Assert.assertThat(teamResults.get(3L).getResults().get(1L).getPenalty(), is(251L));
        Assert.assertThat(teamResults.get(3L).getResults().get(1L).submissionCount(), is(1));
    }

    @Test
    public void addNewSubmissionsTest() throws NoSuchFieldException, IllegalAccessException {

        ContestUpdatesEventProducer eventHandler = mock(ContestUpdatesEventProducer.class);
        doNothing().when(eventHandler).publishEvent(any());

        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        Class aClass = ContestDataStorage.class;
        Field field = aClass.getDeclaredField("contestUpdatesEventProducer");
        field.setAccessible(true);
        field.set(storage, eventHandler);

        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, false);

        List<SubmissionNode> updatedSubmissions = new ArrayList<>(new DefaultObjects().getSubmissionNodes());
        updatedSubmissions.add(new MockedObjectGenerator().defaultSubmissionNode().withId(8L).withProblemId(4L).withRunUuid("8").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(5L).build());
        ContestNode updatedContest = getDefaultMockedContestBuilder().withSubmissions(updatedSubmissions).build();

        storage.updateContest(contest.getContestId(), updatedContest, false);
        Contest contestData = storage.getContestData(contest.getContestId());
        Map<Long, ParticipantResult> teamResults = contestData.getTeamsResults(Arrays.asList(5L));
        Assert.assertThat(teamResults.get(5L).solvedProblems(), is(3));

        Assert.assertThat(teamResults.get(5L).getResults().get(4L).getStatus(), is(SubmissionStatus.OK));
        Assert.assertThat(teamResults.get(5L).getResults().get(4L).getPenalty(), is(151L));
        Assert.assertThat(teamResults.get(5L).getResults().get(4L).submissionCount(), is(1));
    }

    @Test
    public void updateTeamInfoTest() {
        ContestNode contestNode = getDefaultMockedContestBuilder().build();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contestNode.getContestId(), contestNode, true);
        Assert.assertThat(storage.getContestData(contestNode.getContestId()).getResults().size(), is(new DefaultObjects().getParticipantNodes().size()));

        ArrayList<ParticipantNode> participants = new ArrayList<>(new DefaultObjects().getParticipantNodes());
        participants.add(new MockedObjectGenerator().defaultParticipantNode().withId(6L).withName("Test team 6 updated").build());
        ContestNode updatedContest = getDefaultMockedContestBuilder().withParticipants(participants).build();
        storage.updateContest(updatedContest.getContestId(), updatedContest, true);
        List<ParticipantResult> results = storage.getContestData(updatedContest.getContestId()).getResults();
        Optional<ParticipantResult> participantResult = results.stream().filter(result -> result.getParticipant().getId().equals(6L)).findFirst();
        Assert.assertThat(participantResult.isPresent(), is(true));
        Assert.assertThat(participantResult.get().getParticipant().getName(), is("Test team 6 updated"));
    }

    @Test
    public void addNewTeamTest() {
        ContestNode contestNode = getDefaultMockedContestBuilder().build();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contestNode.getContestId(), contestNode, true);
        Assert.assertThat(storage.getContestData(contestNode.getContestId()).getResults().size(), is(new DefaultObjects().getParticipantNodes().size()));

        ArrayList<ParticipantNode> participants = new ArrayList<>(new DefaultObjects().getParticipantNodes());
        participants.add(new MockedObjectGenerator().defaultParticipantNode().withId(6L).withName("Test team 6").build());
        ContestNode updatedContest = getDefaultMockedContestBuilder().withParticipants(participants).build();
        storage.updateContest(updatedContest.getContestId(), updatedContest, true);
        Assert.assertThat(storage.getContestData(updatedContest.getContestId()).getResults().size(), is(participants.size()));
    }

    @Test
    public void updateProblemInfoTest() {
        ContestNode contestNode = getDefaultMockedContestBuilder().build();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contestNode.getContestId(), contestNode, true);
        List<ProblemNode> problemNodes = contestNode.getProblems();
        problemNodes.set(1, new MockedObjectGenerator().defaultProblemNode().withId(2L).withLongName("Test task 2 changed").withShortName("B").build());

        storage.updateContest(contestNode.getContestId(), contestNode, true);

        Assert.assertThat(storage.getContestData(1L).getTasks().get(1).getShortName(), is("B"));
        Assert.assertThat(storage.getContestData(1L).getTasks().get(1).getLongName(), is("Test task 2 changed"));
        Assert.assertThat(storage.getContestData(1L).getTasks().get(1).getId(), is(2L));
    }

    @Test
    public void addNewProblemTest() {
        ContestNode contestNode = getDefaultMockedContestBuilder().build();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contestNode.getContestId(), contestNode, true);
        List<ProblemNode> problemNodes = new ArrayList<>(contestNode.getProblems());

        problemNodes.add(3, new MockedObjectGenerator().defaultProblemNode().withId(4L).withLongName("Test task 4").withShortName("E").build());
        contestNode = getDefaultMockedContestBuilder().withProblems(problemNodes).build();

        storage.updateContest(contestNode.getContestId(), contestNode, true);
        Assert.assertThat(storage.getContestData(1L).getTasks().size(), is(4));
        Assert.assertThat(storage.getContestData(1L).getTasks().get(3).getShortName(), is("E"));
        Assert.assertThat(storage.getContestData(1L).getTasks().get(3).getLongName(), is("Test task 4"));
        Assert.assertThat(storage.getContestData(1L).getTasks().get(3).getId(), is(4L));
    }

    @Test
    public void getFrozenResultsOnFrozenContestTest() {
        ContestNode contestNode = getDefaultMockedContestBuilder().build();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contestNode.getContestId(), contestNode, true);
        Assert.assertThat(storage.getFrozenSubmits(1L).size(), is(3));
        Contest contest = storage.getContestData(contestNode.getContestId());
        List<SubmissionNode> submissions = contest.getResults().stream()
                .flatMap(result -> result.getResults().values().stream().flatMap(res -> res.getSubmissions().stream()))
                .filter(item -> item.getStatus() == SubmissionStatus.FROZEN)
                .collect(Collectors.toList());

        Assert.assertThat(submissions.size(), is(3));
    }

    @Test
    public void getFrozenResultsOnUnfrozenContestTest() {
        ContestNode contestNode = getDefaultMockedContestBuilder().build();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contestNode.getContestId(), contestNode, false);
        Assert.assertThat(storage.getFrozenSubmits(1L).size(), is(3));

        Contest contest = storage.getContestData(contestNode.getContestId());
        List<SubmissionNode> submissions = contest.getResults().stream()
                .flatMap(result -> result.getResults().values().stream().flatMap(res -> res.getSubmissions().stream()))
                .filter(item -> item.getStatus() == SubmissionStatus.FROZEN)
                .collect(Collectors.toList());

        Assert.assertThat(submissions.size(), is(0));
    }

    @Test
    @Ignore
    public void readNewStandingsFileFromTheSameSource() {

    }
}