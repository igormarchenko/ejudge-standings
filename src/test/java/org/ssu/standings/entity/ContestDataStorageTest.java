package org.ssu.standings.entity;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.ssu.standings.DefaultObjects;
import org.ssu.standings.MockedObjectGenerator;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.ParticipantResult;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.ParticipantNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;


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

    public void addNewSubmissionsTest() {

        ContestNode contest = getContestNode();
        ContestDataStorage storage = new ContestDataStorage();
        storage.setTeams(new MockedObjectGenerator().getTeamList());
        storage.updateContest(contest.getContestId(), contest, false);

        List<SubmissionNode> updatedSubmissions = new ArrayList<>(new DefaultObjects().getSubmissionNodes());
        updatedSubmissions.add(new MockedObjectGenerator().defaultSubmissionNode().withId(8L).withProblemId(4L).withRunUuid("8").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(5L).build());
        ContestNode updatedContest = getDefaultMockedContestBuilder().withSubmissions(updatedSubmissions).build();

        storage.updateContest(contest.getContestId(), updatedContest, false);
        Contest contestData = storage.getContestData(contest.getContestId());
        Map<Long, ParticipantResult> teamResults = contestData.getTeamsResults(Arrays.asList(5L));
        Assert.assertThat(teamResults.get(5L).solvedProblems(), is(3L));

        Assert.assertThat(teamResults.get(5L).getResults().get(4L).getStatus(), is(SubmissionStatus.OK));
        Assert.assertThat(teamResults.get(5L).getResults().get(4L).getPenalty(), is(151L));
        Assert.assertThat(teamResults.get(5L).getResults().get(4L).submissionCount(), is(1));


    }

    @Test
    @Ignore
    public void rejudgeSubmissionsTest() {

    }

    @Test
    @Ignore
    public void updateTeamInfoTest() {

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
    @Ignore
    public void updateProblemInfoTest() {

    }

    @Test
    @Ignore
    public void addNewProblemTest() {

    }

    @Test
    @Ignore
    public void getFrozenResultsOnFrozenContestTest() {

    }

    @Test
    @Ignore
    public void getFrozenResultsOnUnfrozenContestTest() {

    }

    @Test
    @Ignore
    public void readNewStandingsFileFromTheSameSource() {

    }
}