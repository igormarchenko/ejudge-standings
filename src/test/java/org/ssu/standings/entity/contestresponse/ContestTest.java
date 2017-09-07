package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.ssu.standings.MockedObjectGenerator;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;

public class ContestTest {

    public Contest getContest() {
        return new MockedObjectGenerator().defaultMockedContest();
    }



    @Test
    public void getResults() throws Exception {
        Contest contest = getContest();
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
        Contest contest = getContest();
        Map<Long, ParticipantResult> results = contest.getTeamsResults(Arrays.asList(1L, 2L, 5L));
        Assert.assertThat(results.size(), is(3));
        Assert.assertThat(results.get(1L).getResults().size(), is(0));
        Assert.assertThat(results.get(2L).getResults().size(), is(1));
        Assert.assertThat(results.get(5L).getResults().size(), is(2));
    }

    @Test
    public void getTeamsResultsOnEmptyList() throws Exception {
        Contest contest = getContest();
        Map<Long, ParticipantResult> results = contest.getTeamsResults(Arrays.asList());
        Assert.assertNotNull(results);
        Assert.assertThat(results.size(), is(0));
    }


    @Test
    public void addSingleSuccessfullSubmission() throws Exception {
        Contest contest = getContest();
        Contest copy = new Contest.Builder(contest).build();

        SubmissionNode submissionNode = new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(1L).withRunUuid("1").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(1L).build();
        copy.updateSubmissions(Arrays.asList(submissionNode));
        Assert.assertThat(copy.getResults().get(3).getParticipant().getId(), is(1L));
    }

    @Test
    public void addSingleWrongSubmission() throws Exception {
        Contest contest = getContest();
        Contest copy = new Contest.Builder(contest).build();

        SubmissionNode submissionNode = new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(2L).withRunUuid("1").withStatus(SubmissionStatus.WA).withTime(60 * 250L).withUserId(1L).build();
        copy.updateSubmissions(Arrays.asList(submissionNode));
        Assert.assertThat(copy.getResults().get(3).getParticipant().getId(), is(1L));
    }


    @Test
    public void addRejudgedSubmission() throws Exception {
        Contest contest = getContest();
        Contest copy = new Contest.Builder(contest).build();

        SubmissionNode submissionNode = new MockedObjectGenerator().defaultSubmissionNode().withId(2L).withProblemId(1L).withRunUuid("2").withStatus(SubmissionStatus.OK).withTime(60 * 100L).withUserId(3L).build();
        copy.updateSubmissions(Arrays.asList(submissionNode));
        Assert.assertThat(copy.getResults().get(2).getParticipant().getId(), is(3L));
    }

    @Test
    public void addSubmissionList() throws Exception {
        Contest contest = getContest();
        List<SubmissionNode> nodes = Arrays.asList(new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(1L).withRunUuid("12").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(3L).build(),
                new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(2L).withRunUuid("13").withStatus(SubmissionStatus.WA).withTime(60 * 250L).withUserId(3L).build(),
                new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(2L).withRunUuid("14").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(3L).build(),
                new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(3L).withRunUuid("15").withStatus(SubmissionStatus.OK).withTime(60 * 250L).withUserId(3L).build());

        contest.updateSubmissions(nodes);
        Assert.assertThat(contest.getResults().get(0).getParticipant().getId(), is(3L));
    }

    @Test
    public void getContestId() throws Exception {
        Contest contest = getContest();
        Assert.assertThat(contest.getContestId(), is(1L));
    }

    @Test
    public void getName() throws Exception {
        Contest contest = getContest();
        Assert.assertThat(contest.getName(), is("Test contest"));
    }

    @Test
    public void getDuration() throws Exception {
        Contest contest = getContest();
        Assert.assertThat(contest.getDuration(), is(3600L));
    }

    @Test
    public void getStartTime() throws Exception {
        Contest contest = getContest();
        Assert.assertThat(contest.getStartTime(), is(LocalDateTime.of(2017, 3, 25, 3, 57, 10)));
    }

    @Test
    public void getStopTime() throws Exception {
        Contest contest = getContest();
        Assert.assertThat(contest.getStopTime(), is(LocalDateTime.of(2017, 3, 25, 8, 57, 10)));
    }

    @Test
    public void getCurrentTime() throws Exception {
        Contest contest = getContest();
        Assert.assertThat(contest.getCurrentTime(), is(LocalDateTime.of(2018, 4, 25, 4, 57, 10)));
    }

    @Test
    public void getFogTime() throws Exception {
        Contest contest = getContest();
        Assert.assertThat(contest.getFogTime(), is(100L));
    }

    @Test
    public void getUnfogTime() throws Exception {
        Contest contest = getContest();
        Assert.assertThat(contest.getUnfogTime(), is(200L));
    }

    @Test
    public void getTasks() throws Exception {
        Contest contest = getContest();
        Assert.assertNotNull(contest.getTasks());
        Assert.assertThat(contest.getTasks().size(), is(3));

        Assert.assertThat(contest.getTasks().get(0).getId(), is(1L));
        Assert.assertThat(contest.getTasks().get(1).getId(), is(2L));
        Assert.assertThat(contest.getTasks().get(2).getId(), is(3L));
    }


    @Test
    public void cloneTest() throws NoSuchFieldException, IllegalAccessException {
        Contest contest = getContest();
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


    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Contest contest = getContest();
        String actualJson = mapper.writeValueAsString(contest);
        Assert.assertThat(mapper.readTree(actualJson).size(), is(10));

        Assert.assertNotNull(mapper.readTree(actualJson).get("id"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("name"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("duration"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("startTime"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("stopTime"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("currentTime"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("fogTime"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("unfogTime"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("tasks"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("results"));

        Assert.assertThat(mapper.readTree(actualJson).get("id").asInt(), is(1));
        Assert.assertThat(mapper.readTree(actualJson).get("name").asText(), is("Test contest"));
        Assert.assertThat(mapper.readTree(actualJson).get("duration").asInt(), is(3600));
        Assert.assertThat(mapper.readTree(actualJson).get("fogTime").asLong(), is(100L));
        Assert.assertThat(mapper.readTree(actualJson).get("unfogTime").asLong(), is(200L));
        Assert.assertThat(mapper.readTree(actualJson).get("tasks").size(), is(3));
        Assert.assertThat(mapper.readTree(actualJson).get("results").size(), is(5));


    }

}