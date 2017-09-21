package org.ssu.standings;

import org.mockito.Mockito;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.ParticipantNode;
import org.ssu.standings.parser.entity.ProblemNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class MockedObjectGenerator {

    public Map<String, TeamDAO> getTeamList() {
        Map<String, TeamDAO> teams = new HashMap<>();
        teams.put("team 1", defaultTeamDAO().withId(1L).withName("team 1").build());
        teams.put("team 2", defaultTeamDAO().withId(2L).withName("team 2").build());
        teams.put("team 3", defaultTeamDAO().withId(3L).withName("team 3").build());
        teams.put("team 4", defaultTeamDAO().withId(4L).withName("team 4").build());
        teams.put("team 5", defaultTeamDAO().withId(5L).withName("team 5").build());
        return teams;
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

    public MockedContestNodeBuilder defaultContestNode() {
        return new MockedContestNodeBuilder();
    }

    public class MockedContestNodeBuilder {
        private ContestNode contestNode;

        public MockedContestNodeBuilder() {
            this.contestNode = mock(ContestNode.class);
        }

        public MockedContestNodeBuilder withId(Long value) {
            when(contestNode.getContestId()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withDuration(Long value) {
            when(contestNode.getDuration()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withName(String value) {
            when(contestNode.getName()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withFogTime(Long value) {
            when(contestNode.getFogTime()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withUnfogTime(Long value) {
            when(contestNode.getStartTime()).thenReturn(LocalDateTime.of(2017, 3, 25, 3, 57, 10));
            when(contestNode.getStopTime()).thenReturn(LocalDateTime.of(2017, 3, 25, 8, 57, 10));
            when(contestNode.getCurrentTime()).thenReturn(LocalDateTime.of(2018, 4, 25, 4, 57, 10));
            when(contestNode.getUnfogTime()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withCurrentTime(LocalDateTime value) {
            when(contestNode.getCurrentTime()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withStartTime(LocalDateTime value) {
            when(contestNode.getStartTime()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withStopTime(LocalDateTime value) {
            when(contestNode.getStopTime()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withProblems(List<ProblemNode> value) {
            when(contestNode.getProblems()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withParticipants(List<ParticipantNode> value) {
            when(contestNode.getParticipants()).thenReturn(value);
            return this;
        }

        public MockedContestNodeBuilder withSubmissions(List<SubmissionNode> value) {
            when(contestNode.getSubmissions()).thenReturn(value);
            return this;
        }

        public ContestNode build() {
            return contestNode;
        }
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
            when(mockedSubmissionNode.getStatus()).thenCallRealMethod();
            doCallRealMethod().when(mockedSubmissionNode).setStatus(Mockito.any(SubmissionStatus.class));
            mockedSubmissionNode.setStatus(status);
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
