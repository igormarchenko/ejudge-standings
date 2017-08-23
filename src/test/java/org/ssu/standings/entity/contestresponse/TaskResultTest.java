package org.ssu.standings.entity.contestresponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

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
        unsolvedTask = new TaskResult.Builder().withSubmissions(unsolvedProblemSubmissions).build();

        emptySubmissionListTask = new TaskResult.Builder().withSubmissions(new ArrayList<>()).build();

        List<SubmissionNode> solvedProblemSubmissions = Arrays.asList(
                new SubmissionNode.Builder().withStatus(SubmissionStatus.CE).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.WA).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.ML).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.PE).withTime(123L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withTime(150L).build(),
                new SubmissionNode.Builder().withStatus(SubmissionStatus.SE).withTime(1223L).build()
        );
        solvedTask = new TaskResult.Builder().withSubmissions(solvedProblemSubmissions).build();

        List<SubmissionNode> solvedFirstAttemptProblemSubmissions = Arrays.asList(
                new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withTime(1L).build()
        );
        solvedFirstAttemptTask = new TaskResult.Builder().withSubmissions(solvedFirstAttemptProblemSubmissions).build();
    }

    @Test
    public void submissionCount() throws Exception {
        Assert.assertThat(unsolvedTask.submissionCount(), is(6));
        Assert.assertThat(emptySubmissionListTask.submissionCount(), is(0));
        Assert.assertThat(solvedTask.submissionCount(), is(4));
        Assert.assertThat(solvedFirstAttemptTask.submissionCount(), is(0));
    }

    @Test
    public void getFirstAcceptedTime() throws Exception {
        Assert.assertThat(unsolvedTask.getFirstAcceptedTime(), is(0L));
        Assert.assertThat(emptySubmissionListTask.getFirstAcceptedTime(), is(0L));
        Assert.assertThat(solvedTask.getFirstAcceptedTime(), is(3L));
        Assert.assertThat(solvedFirstAttemptTask.getFirstAcceptedTime(), is(1L));
    }

    @Test
    public void getStatus() throws Exception {
        Assert.assertThat(unsolvedTask.getStatus(), is(SubmissionStatus.SE));
        Assert.assertThat(emptySubmissionListTask.getStatus(), is(SubmissionStatus.EMPTY));
        Assert.assertThat(solvedTask.getStatus(), is(SubmissionStatus.OK));
        Assert.assertThat(solvedFirstAttemptTask.getStatus(), is(SubmissionStatus.OK));
    }

    @Test
    public void getPenalty() throws Exception {
        Assert.assertThat(unsolvedTask.getPenalty(), is(0L));
        Assert.assertThat(emptySubmissionListTask.getPenalty(), is(0L));
        Assert.assertThat(solvedTask.getPenalty(), is(3L + 20L * 3));
        Assert.assertThat(solvedFirstAttemptTask.getPenalty(), is(1L));
    }

    @Test
    public void isProblemSolved() throws Exception {
        Assert.assertThat(unsolvedTask.isProblemSolved(), is(false));
        Assert.assertThat(emptySubmissionListTask.isProblemSolved(), is(false));
        Assert.assertThat(solvedFirstAttemptTask.isProblemSolved(), is(true));
        Assert.assertThat(solvedTask.isProblemSolved(), is(true));
    }

    @Test
    public void addSubmission() throws Exception {
        List<SubmissionNode> submissions = Arrays.asList(
                new SubmissionNode.Builder().withRunUuid("1").withStatus(SubmissionStatus.CE).withTime(123L).build(),
                new SubmissionNode.Builder().withRunUuid("2").withStatus(SubmissionStatus.WA).withTime(123L).build(),
                new SubmissionNode.Builder().withRunUuid("3").withStatus(SubmissionStatus.TL).withTime(123L).build(),
                new SubmissionNode.Builder().withRunUuid("4").withStatus(SubmissionStatus.ML).withTime(123L).build(),
                new SubmissionNode.Builder().withRunUuid("5").withStatus(SubmissionStatus.PE).withTime(123L).build(),
                new SubmissionNode.Builder().withRunUuid("6").withStatus(SubmissionStatus.RT).withTime(123L).build(),
                new SubmissionNode.Builder().withRunUuid("7").withStatus(SubmissionStatus.SE).withTime(123L).build()
        ).stream().collect(Collectors.toList());
        TaskResult taskResult = new TaskResult.Builder().withSubmissions(submissions).build();

        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("8").withStatus(SubmissionStatus.CE).withTime(123L).build());

        Assert.assertThat(taskResult.submissionCount(), is(6));

        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("9").withStatus(SubmissionStatus.WA).withTime(123L).build());
        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("10").withStatus(SubmissionStatus.ML).withTime(123L).build());

        Assert.assertThat(taskResult.submissionCount(), is(8));

        taskResult.addSubmission(new SubmissionNode.Builder().withRunUuid("3").withStatus(SubmissionStatus.OK).withTime(123L).build());
        Assert.assertThat(taskResult.submissionCount(), is(2));
    }

    @Test
    public void cloneTest() throws Exception {
        TaskResult copy = unsolvedTask.clone();
        Assert.assertNotSame(copy, unsolvedTask);
        Assert.assertNotSame(copy.getSubmissions(), unsolvedTask.getSubmissions());
        for(int i = 0; i < copy.getSubmissions().size();i++) {
            Assert.assertNotSame(copy.getSubmissions().get(i), unsolvedTask.getSubmissions().get(i));
        }
    }

}