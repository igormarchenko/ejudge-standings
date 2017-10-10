package org.ssu.standings;

import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.ParticipantNode;
import org.ssu.standings.parser.entity.ProblemNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DefaultObjects {
    private List<ProblemNode> problemNodes = Arrays.asList(
            new MockedObjectGenerator().defaultProblemNode().withId(1L).withLongName("Test task 1").withShortName("A").build(),
            new MockedObjectGenerator().defaultProblemNode().withId(2L).withLongName("Test task 2").withShortName("B").build(),
            new MockedObjectGenerator().defaultProblemNode().withId(3L).withLongName("Test task 3").withShortName("C").build()
    );

    private List<ParticipantNode> participantNodes = Arrays.asList(
            new MockedObjectGenerator().defaultParticipantNode().withId(1L).withName("team 1").build(),
            new MockedObjectGenerator().defaultParticipantNode().withId(2L).withName("team 2").build(),
            new MockedObjectGenerator().defaultParticipantNode().withId(3L).withName("team 3").build(),
            new MockedObjectGenerator().defaultParticipantNode().withId(4L).withName("team 4").build(),
            new MockedObjectGenerator().defaultParticipantNode().withId(5L).withName("team 5").build()
    );


    private List<SubmissionNode> submissionNodes = Arrays.asList(
            new MockedObjectGenerator().defaultSubmissionNode().withId(1L).withProblemId(1L).withRunUuid("1").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(2L).withUserName("team 2").build(),

            new MockedObjectGenerator().defaultSubmissionNode().withId(2L).withProblemId(1L).withRunUuid("2").withStatus(SubmissionStatus.WA).withTime(60 * 100L).withUserId(3L).withUserName("team 3").build(),

            new MockedObjectGenerator().defaultSubmissionNode().withId(3L).withProblemId(1L).withRunUuid("3").withStatus(SubmissionStatus.WA).withTime(60 * 20L).withUserId(4L).withUserName("team 4").build(),
            new MockedObjectGenerator().defaultSubmissionNode().withId(4L).withProblemId(1L).withRunUuid("4").withStatus(SubmissionStatus.OK).withTime(60 * 80L).withUserId(4L).withUserName("team 4").build(),
            new MockedObjectGenerator().defaultSubmissionNode().withId(5L).withProblemId(2L).withRunUuid("5").withStatus(SubmissionStatus.OK).withTime(60 * 100L).withUserId(4L).withUserName("team 4").build(),

            new MockedObjectGenerator().defaultSubmissionNode().withId(6L).withProblemId(1L).withRunUuid("6").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(5L).withUserName("team 5").build(),
            new MockedObjectGenerator().defaultSubmissionNode().withId(7L).withProblemId(2L).withRunUuid("7").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(5L).withUserName("team 5").build()
    );

    public MockedObjectGenerator.MockedContestNodeBuilder getDefaultMockedContestBuilder() {
        return new MockedObjectGenerator().defaultContestNode()
                .withId(1L)
                .withDuration(10800L)
                .withName("Test contest")
                .withFogTime(3600L)
                .withUnfogTime(7200L)
                .withCurrentTime(LocalDateTime.of(2018, 4, 25, 4, 57, 10))
                .withStartTime(LocalDateTime.of(2017, 3, 25, 3, 57, 10))
                .withStopTime(LocalDateTime.of(2017, 3, 25, 6, 57, 10))
                .withSubmissions(submissionNodes)
                .withParticipants(participantNodes)
                .withProblems(problemNodes);
    }

    public ContestNode getContestNode() {
        return getDefaultMockedContestBuilder().build();
    }

    public List<ProblemNode> getProblemNodes() {
        return problemNodes;
    }

    public List<ParticipantNode> getParticipantNodes() {
        return participantNodes;
    }

    public List<SubmissionNode> getSubmissionNodes() {
        return submissionNodes;
    }
}
