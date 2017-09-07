package org.ssu.standings;

import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.ParticipantNode;
import org.ssu.standings.parser.entity.ProblemNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedObjectGenerator {

    public Contest defaultMockedContest() {

        ContestNode newContest;
        List<ProblemNode> problemNodes;
        List<ParticipantNode> participantNodes;
        List<SubmissionNode> submissionNodes;
        problemNodes = Arrays.asList(
                defaultProblemNode().withId(1L).withLongName("Test task 1").withShortName("A").build(),
                defaultProblemNode().withId(2L).withLongName("Test task 2").withShortName("B").build(),
                defaultProblemNode().withId(3L).withLongName("Test task 3").withShortName("C").build()
        );

        participantNodes = Arrays.asList(
                defaultParticipantNode().withId(1L).withName("Test team 1").build(),
                defaultParticipantNode().withId(2L).withName("Test team 2").build(),
                defaultParticipantNode().withId(3L).withName("Test team 3").build(),
                defaultParticipantNode().withId(4L).withName("Test team 4").build(),
                defaultParticipantNode().withId(5L).withName("Test team 5").build()
        );


        submissionNodes = Arrays.asList(
                defaultSubmissionNode().withId(1L).withProblemId(1L).withRunUuid("1").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(2L).build(),

                defaultSubmissionNode().withId(2L).withProblemId(1L).withRunUuid("2").withStatus(SubmissionStatus.WA).withTime(60 * 100L).withUserId(3L).build(),

                defaultSubmissionNode().withId(3L).withProblemId(1L).withRunUuid("3").withStatus(SubmissionStatus.WA).withTime(60 * 20L).withUserId(4L).build(),
                defaultSubmissionNode().withId(4L).withProblemId(1L).withRunUuid("4").withStatus(SubmissionStatus.OK).withTime(60 * 80L).withUserId(4L).build(),
                defaultSubmissionNode().withId(5L).withProblemId(2L).withRunUuid("5").withStatus(SubmissionStatus.OK).withTime(60 * 100L).withUserId(4L).build(),

                defaultSubmissionNode().withId(6L).withProblemId(1L).withRunUuid("6").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(5L).build(),
                defaultSubmissionNode().withId(7L).withProblemId(2L).withRunUuid("7").withStatus(SubmissionStatus.OK).withTime(60 * 150L).withUserId(5L).build()
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
        teams.put("team 1", defaultTeamDAO().withId(1L).withName("team 1").build());
        teams.put("team 2", defaultTeamDAO().withId(2L).withName("team 2").build());
        teams.put("team 3", defaultTeamDAO().withId(3L).withName("team 3").build());
        teams.put("team 4", defaultTeamDAO().withId(4L).withName("team 4").build());
        teams.put("team 5", defaultTeamDAO().withId(5L).withName("team 5").build());
        return new Contest.Builder(newContest, teams).build();
    }

    public MockedProblemNodeBuilder defaultProblemNode() {
        return new MockedProblemNodeBuilder();
    }

    public MockedParticipantNodeBuilder defaultParticipantNode() {
        return new MockedParticipantNodeBuilder();
    }

    public MockedSubmissionNodeBuilder defaultSubmissionNode() {
        return new MockedSubmissionNodeBuilder();
    }

    public MockedTeamDAOBuilder defaultTeamDAO() {
        return new MockedTeamDAOBuilder().withUniversity(null);
    }

    public class MockedProblemNodeBuilder {

        private ProblemNode mockedProblemNode;

        public MockedProblemNodeBuilder() {
            this.mockedProblemNode = mock(ProblemNode.class);
        }

        public MockedProblemNodeBuilder withId(Long value) {
            when(mockedProblemNode.getId()).thenReturn(value);
            return this;
        }

        public MockedProblemNodeBuilder withShortName(String value) {
            when(mockedProblemNode.getShortName()).thenReturn(value);
            return this;
        }

        public MockedProblemNodeBuilder withLongName(String value) {
            when(mockedProblemNode.getLongName()).thenReturn(value);
            return this;
        }

        public ProblemNode build() {
            return mockedProblemNode;
        }

    }

    public class MockedParticipantNodeBuilder {
        private ParticipantNode mockedParticipantNode;

        public MockedParticipantNodeBuilder() {
            this.mockedParticipantNode = mock(ParticipantNode.class);
        }

        public MockedParticipantNodeBuilder withId(Long value) {
            when(mockedParticipantNode.getId()).thenReturn(value);
            return this;
        }

        public MockedParticipantNodeBuilder withName(String value) {
            when(mockedParticipantNode.getName()).thenReturn(value);
            return this;
        }

        public ParticipantNode build() {
            return mockedParticipantNode;
        }
    }

    public class MockedSubmissionNodeBuilder {
        private SubmissionNode mockedSubmissionNode;

        public MockedSubmissionNodeBuilder() {
            this.mockedSubmissionNode = mock(SubmissionNode.class);
        }

        public MockedSubmissionNodeBuilder withId(Long value) {
            when(mockedSubmissionNode.getId()).thenReturn(value);
            return this;
        }

        public MockedSubmissionNodeBuilder withTime(Long time) {
            when(mockedSubmissionNode.getTime()).thenReturn(time);
            return this;
        }

        public MockedSubmissionNodeBuilder withRunUuid(String runUuid) {
            when(mockedSubmissionNode.getRunUuid()).thenReturn(runUuid);
            return this;
        }

        public MockedSubmissionNodeBuilder withStatus(SubmissionStatus status) {
            when(mockedSubmissionNode.getStatus()).thenReturn(status);
            return this;
        }

        public MockedSubmissionNodeBuilder withUserId(Long userId) {
            when(mockedSubmissionNode.getUserId()).thenReturn(userId);
            return this;
        }

        public MockedSubmissionNodeBuilder withProblemId(Long problemId) {
            when(mockedSubmissionNode.getProblemId()).thenReturn(problemId);
            return this;
        }

        public SubmissionNode build() {
            return mockedSubmissionNode;
        }
    }

    public class MockedTeamDAOBuilder {
        private TeamDAO mockedTeamDao;

        public MockedTeamDAOBuilder() {
            this.mockedTeamDao = mock(TeamDAO.class);
        }

        public MockedTeamDAOBuilder withId(Long value) {
            when(mockedTeamDao.getId()).thenReturn(value);
            return this;
        }

        public MockedTeamDAOBuilder withName(String value) {
            when(mockedTeamDao.getName()).thenReturn(value);
            return this;
        }

        public MockedTeamDAOBuilder withUniversity(UniversityDAO value) {
            when(mockedTeamDao.getUniversity()).thenReturn(value);
            return this;
        }

        public TeamDAO build() {
            return mockedTeamDao;
        }
    }


}
