package org.ssu.standings.entity.score;

import org.junit.Assert;
import org.junit.Test;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class KirovScoreCalculatorTest {
    List<SubmissionNode> submissions = new ArrayList<>(Arrays.asList(
            new SubmissionNode.Builder().withRunUuid("1").withStatus(SubmissionStatus.CE).withTime(123L).withScore(0L).build(),
            new SubmissionNode.Builder().withRunUuid("2").withStatus(SubmissionStatus.WA).withTime(124L).withScore(60L).build(),
            new SubmissionNode.Builder().withRunUuid("3").withStatus(SubmissionStatus.TL).withTime(125L).withScore(30L).build(),
            new SubmissionNode.Builder().withRunUuid("4").withStatus(SubmissionStatus.ML).withTime(126L).withScore(80L).build(),
            new SubmissionNode.Builder().withRunUuid("5").withStatus(SubmissionStatus.PE).withTime(127L).withScore(30L).build(),
            new SubmissionNode.Builder().withRunUuid("6").withStatus(SubmissionStatus.RT).withTime(128L).withScore(20L).build(),
            new SubmissionNode.Builder().withRunUuid("7").withStatus(SubmissionStatus.SE).withTime(129L).withScore(10L).build(),
            new SubmissionNode.Builder().withRunUuid("7").withStatus(SubmissionStatus.OK).withTime(129L).withScore(100L).build()
    ));

    @Test
    public void tries() throws Exception {
        Assert.assertThat(new KirovScoreCalculator().tries(submissions), is(7));
    }

    @Test
    public void acceptedTime() throws Exception {
    }

    @Test
    public void status() throws Exception {
        Assert.assertThat(new KirovScoreCalculator().status(submissions), is(SubmissionStatus.OK));
    }

    @Test
    public void penalty() throws Exception {
        Assert.assertThat(new KirovScoreCalculator().penalty(submissions), is(100L));
        Assert.assertThat(new KirovScoreCalculator().penalty(submissions.subList(0, 6)), is(80L));
    }

    @Test
    public void isProblemSolved() throws Exception {
        Assert.assertThat(new KirovScoreCalculator().isProblemSolved(submissions), is(true));
        Assert.assertThat(new KirovScoreCalculator().isProblemSolved(submissions.subList(0, 6)), is(false));
    }

}