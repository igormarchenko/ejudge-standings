package org.ssu.standings;

import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.ParticipantNode;
import org.ssu.standings.parser.entity.ProblemNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedObjectGenerator {
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
