package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class ParticipantResultTest {

    List<SubmissionNode> submissions;
    private ParticipantResult result = new ParticipantResult.Builder().withParticipant(null).build();

    @Before
    public void setUp() throws Exception {
        result = new ParticipantResult.Builder().withParticipant(new Participant.Builder().build()).build();
        submissions = Arrays.asList(
                new SubmissionNode.Builder().withRunUuid("1").withProblemId(1L).withTime(1L).withStatus(SubmissionStatus.CE).build(),
                new SubmissionNode.Builder().withRunUuid("2").withProblemId(1L).withTime(10L).withStatus(SubmissionStatus.WA).build(),
                new SubmissionNode.Builder().withRunUuid("3").withProblemId(1L).withTime(100L).withStatus(SubmissionStatus.TL).build(),

                new SubmissionNode.Builder().withRunUuid("4").withProblemId(2L).withTime(12L).withStatus(SubmissionStatus.TL).build(),
                new SubmissionNode.Builder().withRunUuid("5").withProblemId(2L).withTime(23L).withStatus(SubmissionStatus.WA).build(),
                new SubmissionNode.Builder().withRunUuid("6").withProblemId(2L).withTime(35L).withStatus(SubmissionStatus.OK).build(),
                new SubmissionNode.Builder().withRunUuid("7").withProblemId(2L).withTime(70L).withStatus(SubmissionStatus.TL).build(),
                new SubmissionNode.Builder().withRunUuid("8").withProblemId(2L).withTime(80L).withStatus(SubmissionStatus.OK).build(),

                new SubmissionNode.Builder().withRunUuid("9").withProblemId(3L).withTime(100L).withStatus(SubmissionStatus.CE).build(),
                new SubmissionNode.Builder().withRunUuid("10").withProblemId(3L).withTime(200L).withStatus(SubmissionStatus.WA).build(),
                new SubmissionNode.Builder().withRunUuid("11").withProblemId(3L).withTime(300L).withStatus(SubmissionStatus.OK).build(),

                new SubmissionNode.Builder().withRunUuid("12").withProblemId(4L).withTime(150L).withStatus(SubmissionStatus.OK).build()
        );
        submissions.forEach(submit -> result.pushSubmit(submit));
    }

    @Test
    public void pushSubmitTest() throws Exception {
        ParticipantResult participantResult = result.clone();

        participantResult.pushSubmit(new SubmissionNode.Builder().withRunUuid("13").withProblemId(1L).withTime(200L).withStatus(SubmissionStatus.OK).build());
        Assert.assertThat(participantResult.solvedProblems(), is(4L));
        Assert.assertThat(participantResult.getPenalty(), is(114L));
    }

    @Test
    public void getPenaltyTest() throws Exception {
        Assert.assertThat(result.getPenalty(), is(70L));
    }

    @Test
    public void emptyResultPenaltyTest() throws Exception {
        Assert.assertThat(new ParticipantResult.Builder().withParticipant(null).build().getPenalty(), is(0L));
    }

    @Test
    public void solvedProblemsTest() throws Exception {
        Assert.assertThat(result.solvedProblems(), is(3L));
    }

    @Test
    public void emptyResultSolvedProblemsTest() throws Exception {
        Assert.assertThat(new ParticipantResult.Builder().withParticipant(null).build().solvedProblems(), is(0L));
    }

    @Test
    public void compareToEqualsTest() throws Exception {
        Assert.assertThat(result.compareTo(result), is(0));
    }

    @Test
    public void compareToGreaterPenaltyTest() throws Exception {
        ParticipantResult greater = result.clone();
        ParticipantResult smaller = result.clone();
        smaller.pushSubmit(new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withRunUuid("211").withTime(100L).withProblemId(5L).build());
        greater.pushSubmit(new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withRunUuid("212").withTime(1000L).withProblemId(5L).build());

        Assert.assertThat(smaller.compareTo(greater), lessThan(0));
    }

    @Test
    public void compareToSmallerPenaltyTest() throws Exception {

        ParticipantResult greater = result.clone();
        ParticipantResult smaller = result.clone();
        smaller.pushSubmit(new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withRunUuid("211").withTime(100L).withProblemId(5L).build());
        greater.pushSubmit(new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withRunUuid("212").withTime(1000L).withProblemId(5L).build());

        Assert.assertThat(greater.compareTo(smaller), greaterThan(0));
    }

    @Test
    public void compareToGreaterTasksTest() throws Exception {
        ParticipantResult copy = result.clone();
        copy.pushSubmit(new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withRunUuid("21").withTime(10L).withProblemId(5L).build());
        Assert.assertThat(result.compareTo(copy), greaterThan(0));
    }

    @Test
    public void compareToSmallerTasksTest() throws Exception {
        ParticipantResult copy = result.clone();
        copy.pushSubmit(new SubmissionNode.Builder().withStatus(SubmissionStatus.OK).withRunUuid("21").withTime(10L).withProblemId(5L).build());
        Assert.assertThat(result.compareTo(copy), greaterThan(0));
    }

    @Test
    public void cloneTest() throws Exception {
        ParticipantResult clone = result.clone();
        Assert.assertNotSame(clone.getParticipant(), result.getParticipant());
        Assert.assertNotSame(clone.getResults(), result.getResults());
    }


    @Test
    public void serializeToJsonTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String actualJson = mapper.writeValueAsString(result);

        Assert.assertThat(mapper.readTree(actualJson).size(), is(4));

        Assert.assertNotNull(mapper.readTree(actualJson).get("participant"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("results"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("penalty"));
        Assert.assertNotNull(mapper.readTree(actualJson).get("solved"));


        Assert.assertThat(mapper.readTree(actualJson).get("results").size() > 0, is(true));
        Assert.assertThat(mapper.readTree(actualJson).get("penalty").asInt(), is(70));
        Assert.assertThat(mapper.readTree(actualJson).get("solved").asInt(), is(3));
    }
}