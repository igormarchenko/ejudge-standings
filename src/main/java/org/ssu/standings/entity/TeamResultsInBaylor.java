package org.ssu.standings.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Standing")
public class TeamResultsInBaylor {
    @JacksonXmlProperty(localName = "LastProblemTime", isAttribute = true)
    private Long lastProblemTime = 0L;

    @JacksonXmlProperty(localName = "ProblemsSolved", isAttribute = true)
    private Integer problemsSolved = 0;

    @JacksonXmlProperty(localName = "Rank", isAttribute = true)
    private Integer rank = -1;

    @JacksonXmlProperty(localName = "TeamID", isAttribute = true)
    private Long teamId = -1L;

    @JacksonXmlProperty(localName = "TotalTime", isAttribute = true)
    private Long totalTime = 0L;

    @JacksonXmlProperty(localName = "TeamName", isAttribute = true)
    private String teamName = "";

    public TeamResultsInBaylor() {
    }

    public String getTeamName() {
        return teamName;
    }

    private TeamResultsInBaylor(Builder builder) {
        lastProblemTime = builder.lastProblemTime;
        problemsSolved = builder.problemsSolved;
        rank = builder.rank;
        teamId = builder.teamId;
        totalTime = builder.totalTime;
        teamName = builder.teamName;
    }


    public static final class Builder {
        private Long lastProblemTime;
        private Integer problemsSolved;
        private Integer rank;
        private Long teamId;
        private Long totalTime;
        private String teamName;

        public Builder() {
        }

        public Builder(TeamResultsInBaylor copy) {
            this.lastProblemTime = copy.lastProblemTime;
            this.problemsSolved = copy.problemsSolved;
            this.rank = copy.rank;
            this.teamId = copy.teamId;
            this.totalTime = copy.totalTime;
            this.teamName = copy.teamName;
        }

        public Builder withLastProblemTime(Long lastProblemTime) {
            this.lastProblemTime = lastProblemTime;
            return this;
        }

        public Builder withProblemsSolved(Integer problemsSolved) {
            this.problemsSolved = problemsSolved;
            return this;
        }

        public Builder withRank(Integer rank) {
            this.rank = rank;
            return this;
        }

        public Builder withTeamId(Long teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder withTotalTime(Long totalTime) {
            this.totalTime = totalTime;
            return this;
        }

        public Builder withTeamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public TeamResultsInBaylor build() {
            return new TeamResultsInBaylor(this);
        }
    }
}
