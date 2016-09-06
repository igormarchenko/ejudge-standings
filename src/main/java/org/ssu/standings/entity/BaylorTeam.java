package org.ssu.standings.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Objects;

public class BaylorTeam implements Comparable {

    @JacksonXmlProperty(localName = "LastProblemTime", isAttribute = true)
    private Long lastProblemTime = 0L;

    @JacksonXmlProperty(localName = "ProblemsSolved", isAttribute = true)
    private Integer problemsSolved = 0;

    @JacksonXmlProperty(localName = "Rank", isAttribute = true)
    private Integer rank;

    @JacksonXmlProperty(localName = "TeamID", isAttribute = true)
    private Long teamId;

    @JacksonXmlProperty(localName = "TotalTime", isAttribute = true)
    private Integer totalTime = 0;

    @JacksonXmlProperty(localName = "TeamName", isAttribute = true)
    private String teamName;


    public Long getLastProblemTime() {
        return lastProblemTime;
    }

    public BaylorTeam setLastProblemTime(Long lastProblemTime) {
        this.lastProblemTime = lastProblemTime;
        return this;
    }

    public Integer getProblemsSolved() {
        return problemsSolved;
    }

    public BaylorTeam setProblemsSolved(Integer problemsSolved) {
        this.problemsSolved = problemsSolved;
        return this;
    }

    public Integer getRank() {
        return rank;
    }

    public BaylorTeam setRank(Integer rank) {
        this.rank = rank;
        return this;
    }

    public Long getTeamId() {
        return teamId;
    }

    public BaylorTeam setTeamId(Long teamId) {
        this.teamId = teamId;
        return this;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public BaylorTeam setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    public String getTeamName() {
        return teamName;
    }

    public BaylorTeam setTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    @Override
    public int compareTo(Object o) {
        BaylorTeam team = (BaylorTeam) o;
        if (Objects.equals(team.getProblemsSolved(), problemsSolved))
            return getTotalTime() - team.getTotalTime();
        else
            return team.getProblemsSolved() - problemsSolved;
    }
}
