package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.entity.score.AcmScoreCalculator;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;

public class TaskResultTest {

    private TaskResult unsolvedTask;
    private TaskResult solvedTask;
    private TaskResult emptySubmissionListTask;
    private TaskResult solvedFirstAttemptTask;
    private TaskResult solvedTaskWithoutCE;

    @Before
    public void setUp() throws Exception {
        List<SubmissionNode> unsolvedProblemSubmissions = Arrays.asList(
                new SubmissionNode.Builder().withStatus(SubmissionStatus.CE).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.WA).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.TL).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.ML).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.PE).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.RT).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.SE).withTime(123L).build()
        );
        unsolvedTask = new TaskResult.Builder().withSubmissions(unsolvedProblemSubmissions).withCalculator(new AcmScoreCalculator()).build();

        emptySubmissionListTask = new TaskResult.Builder().withSubmissions(new ArrayList<>()).withCalculator(new AcmScoreCalculator()).build();

        List<SubmissionNode> solvedProblemSubmissions = Arrays.asList(
                new SubmissionNode.Builder().withStatus(SubmissionStatus.CE).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.WA).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.ML).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.PE).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withTime(150L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.SE).withTime(1223L).build()
        );
        solvedTask = new TaskResult.Builder().withSubmissions(solvedProblemSubmissions).withCalculator(new AcmScoreCalculator()).build();


        List<SubmissionNode> solvedProblemWithoutCESubmissions = Arrays.asList(
                new SubmissionNode.Builder().withStatus(SubmissionStatus.WA).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.ML).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.PE).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withTime(150L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.SE).withTime(1223L).build()
        );
        solvedTaskWithoutCE = new TaskResult.Builder().withSubmissions(solvedProblemSubmissions).withCalculator(new AcmScoreCalculator()).build();

        List<SubmissionNode> solvedFirstAttemptProblemSubmissions = Arrays.asList(
                new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withTime(1L).build()
        );
        solvedFirstAttemptTask = new TaskResult.Builder().withSubmissions(solvedFirstAttemptProblemSubmissions).withCalculator(new AcmScoreCalculator()).build();
    }

    @Test
    public void unsolvedTaskSubmissionCount() {
        Assert.assertThat(unsolvedTask.submissionCount(), is(6));
    }

    @Test
    public void emptyTaskSubmissionCount() {
        Assert.assertThat(emptySubmissionListTask.submissionCount(), is(0));
    }

    @Test
    public void solvedTaskSubmissionCount() {
        Assert.assertThat(solvedTask.submissionCount(), is(4));
    }

    @Test
    public void solvedFirstAttemptTaskSubmissionCount() {
        Assert.assertThat(solvedFirstAttemptTask.submissionCount(), is(1));
    }

    @Test
    public void solvedTaskWithoutCESubmissionCount() {
        Assert.assertThat(solvedTaskWithoutCE.submissionCount(), is(4));
    }

    @Test
    public void unsolvedTaskFirstAcceptedTime() {
        Assert.assertThat(unsolvedTask.getFirstAcceptedTime(), is(0L));
    }

    @Test
    public void emptyTaskFirstAcceptedTime() {
        Assert.assertThat(emptySubmissionListTask.getFirstAcceptedTime(), is(0L));
    }

    @Test
    public void solvedTaskFirstAcceptedTime() {
        Assert.assertThat(solvedTask.getFirstAcceptedTime(), is(3L));
    }

    @Test
    public void solvedFirstAttemptTaskFirstAcceptedTime() {
        Assert.assertThat(solvedFirstAttemptTask.getFirstAcceptedTime(), is(1L));
    }

    @Test
    public void solvedTaskWithoutCEFirstAcceptedTime() {
        Assert.assertThat(solvedTaskWithoutCE.getFirstAcceptedTime(), is(3L));
    }

    @Test
    public void unsolvedTaskStatus() {
        Assert.assertThat(unsolvedTask.getStatus(), is(SubmissionStatus.SE));
    }

    @Test
    public void emptyTaskStatus() {
        Assert.assertThat(emptySubmissionListTask.getStatus(), is(SubmissionStatus.EMPTY));
    }

    @Test
    public void solvedTaskStatus() {
        Assert.assertThat(solvedTask.getStatus(), is(SubmissionStatus.OK));
    }

    @Test
    public void solvedFirstAttemptTaskStatus() {
        Assert.assertThat(solvedFirstAttemptTask.getStatus(), is(SubmissionStatus.OK));
    }

    @Test
    public void solvedTaskWithoutCEStatus() {
        Assert.assertThat(solvedTaskWithoutCE.getStatus(), is(SubmissionStatus.OK));
    }

    @Test
    public void unsolvedTaskPenalty() {
        Assert.assertThat(unsolvedTask.getPenalty(), is(0L));
    }

    @Test
    public void emptyTaskPenalty() {
        Assert.assertThat(emptySubmissionListTask.getPenalty(), is(0L));
    }

    @Test
    public void solvedTaskPenalty() {
        Assert.assertThat(solvedTask.getPenalty(), is(3L + 20L * 3));
    }

    @Test
    public void solvedTaskWithoutCEPenalty() {
        Assert.assertThat(solvedTaskWithoutCE.getPenalty(), is(3L + 20L * 3));
    }

    @Test
    public void solvedFirstAttemptTaskPenalty() {
        Assert.assertThat(solvedFirstAttemptTask.getPenalty(), is(1L));
    }

    @Test
    public void unsolvedTaskIsProblemSolved() {
        Assert.assertThat(unsolvedTask.isProblemSolved(), is(false));
    }

    @Test
    public void emptyTaskIsProblemSolved() {
        Assert.assertThat(emptySubmissionListTask.isProblemSolved(), is(false));
    }

    @Test
    public void solvedTaskIsProblemSolved() {
        Assert.assertThat(solvedTask.isProblemSolved(), is(true));
    }

    @Test
    public void solvedTaskWithoutCEIsProblemSolved() {
        Assert.assertThat(solvedTaskWithoutCE.isProblemSolved(), is(true));
    }

    @Test
    public void solvedFirstAttemptTaskIsProblemSolved() {
        Assert.assertThat(solvedFirstAttemptTask.isProblemSolved(), is(true));
    }

    @Test
    public void addSubmission() throws Exception {
        List<SubmissionNode> submissions = Arrays.asList(
                new SubmissionNode.Builder().withRunUuid("1").withStatus(SubmissionStatus.CE).withTime(123L).build(),
                new SubmissionNode.Builder().withRunUuid("2").withStatus(SubmissionStatus.WA).withTime(124L).build(),
                new SubmissionNode.Builder().withRunUuid("3").withStatus(SubmissionStatus.TL).withTime(125L).build(),
                new SubmissionNode.Builder().withRunUuid("4").withStatus(SubmissionStatus.ML).withTime(126L).build(),
                new SubmissionNode.Builder().withRunUuid("5").withStatus(SubmissionStatus.PE).withTime(127L).build(),
                new SubmissionNode.Builder().withRunUuid("6").withStatus(SubmissionStatus.RT).withTime(128L).build(),
                new SubmissionNode.Builder().withRunUuid("7").withStatus(SubmissionStatus.SE).withTime(129L).build()
        ).stream().collect(Collectors.toList());
        TaskResult taskResult = new TaskResult.Builder().withSubmissions(submissions).build();

        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("8").withStatus(SubmissionStatus.CE).withTime(130L).build());

        Assert.assertThat(taskResult.submissionCount(), is(6));

        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("9").withStatus(SubmissionStatus.WA).withTime(131L).build());
        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("10").withStatus(SubmissionStatus.ML).withTime(132L).build());

        Assert.assertThat(taskResult.submissionCount(), is(8));

        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("3").withStatus(SubmissionStatus.OK).withTime(125L).build());
        Assert.assertThat(taskResult.submissionCount(), is(2));
    }


    @Test
    public void addTaskResultToEmptyList() throws Exception {
        TaskResult taskResult = new TaskResult.Builder().build();
        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("1").withStatus(SubmissionStatus.WA).withTime(123L).build());
        Assert.assertThat(taskResult.submissionCount(), is(1));
    }

    @Test
    public void cloneTest() throws Exception {
        TaskResult copy = unsolvedTask.clone();
        Assert.assertNotSame(copy, unsolvedTask);
        Assert.assertNotSame(copy.getSubmissions(), unsolvedTask.getSubmissions());
        for (int i = 0; i < copy.getSubmissions().size(); i++) {
            Assert.assertNotSame(copy.getSubmissions().get(i), unsolvedTask.getSubmissions().get(i));
        }
    }

    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String actualJson = mapper.writeValueAsString(solvedTask);

        Assert.assertThat(mapper.readTree(actualJson).size(), is(4));

        Assert.assertNotNull(mapper.readTree(actualJson).get("tries"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("acceptedTime"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("status"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("penalty"));


        Assert.assertThat(mapper.readTree(actualJson).get("tries").asInt(), is(4));
        Assert.assertThat(mapper.readTree(actualJson).get("acceptedTime").asInt(),is(3));
        Assert.assertThat(mapper.readTree(actualJson).get("status").asText(), is("OK"));
        Assert.assertThat(mapper.readTree(actualJson).get("penalty").asInt(), is(63));


    }

}