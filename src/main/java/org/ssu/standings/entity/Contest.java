package org.ssu.standings.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Contest {
    private List<Submission> submissions;
    private List<Team> teams;
    private List<Task> tasks;
    private Long contestId;

    private String name;
    private String region;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime frozenTime;
    private LocalDateTime unfrozenTime;

    public List<Submission> getSubmissions() {
        return submissions.stream().filter(item -> !"CE".equals(item.getStatus())).collect(Collectors.toList());
    }

    public Contest setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
        return this;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Contest setTeams(List<Team> teams) {
        this.teams = teams;
        return this;
    }

    public Long getContestId() {
        return contestId;
    }

    public Contest setContestId(Long contestId) {
        this.contestId = contestId;
        return this;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public Contest setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Contest setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public LocalDateTime getFrozenTime() {
        return frozenTime;
    }

    public Contest setFrozenTime(LocalDateTime frozenTime) {
        this.frozenTime = frozenTime;
        return this;
    }

    public LocalDateTime getUnfrozenTime() {
        return unfrozenTime;
    }

    public Contest setUnfrozenTime(LocalDateTime unfrozenTime) {
        this.unfrozenTime = unfrozenTime;
        return this;
    }

    public LocalDateTime getSubmissionTime(Long submissionSeconds) {
        return beginTime.plusSeconds(submissionSeconds);
    }

    public Boolean inFrozenTime(LocalDateTime time) {
        return !(time.compareTo(unfrozenTime) >= 0 || time.compareTo(frozenTime) <= 0);

    }
    public Boolean isFrozen() {
        return inFrozenTime(LocalDateTime.now());
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Contest setTasks(List<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public String getName() {
        return name;
    }

    public Contest setName(String name) {
        this.name = name;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Contest setRegion(String region) {
        this.region = region;
        return this;
    }
}
