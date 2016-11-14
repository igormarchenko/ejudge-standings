package org.ssu.standings.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Contest {
    private Long id;
    private List<Submission> submissions;
    private List<Team> teams;
    private List<Task> tasks;
    private Long contestId;

    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime frozenTime;
    private LocalDateTime unfrozenTime;
    private LocalDateTime currentTime;
    private Boolean isFinalResults;

//    private transient List<Submission> frozenSubmits;

    public Long getId() {
        return id;
    }

    public Contest setId(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getFinalResults() {
        return isFinalResults;
    }

    public void setFinalResults(Boolean finalResults) {
        isFinalResults = finalResults;
    }

    public List<Submission> getSubmissions() {
        return submissions.stream().filter(item -> !"CE".equals(item.getStatus())).collect(Collectors.toList());
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public Contest setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
        return this;
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
        return time.compareTo(frozenTime) > 0 && time.compareTo(endTime) <= 0;
    }

    public Boolean isFrozen() {
        return inFrozenTime(currentTime);
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

    public Boolean getIsFinalResults() {
        return isFinalResults;
    }

    public Contest setIsFinalResults(Boolean isFinalResults) {
        this.isFinalResults = isFinalResults;
        return this;
    }

//    public Optional<Team> getTeamId(String teamName) {
//        return teams.stream().filter(team -> team.getName().trim().equals(teamName)).findAny();
//    }
//
//    public List<Submission> getTeamSubmissions(Team team) {
//        return submissions.stream().filter(submission -> submission.getUserId().equals(team.getTeamIdInContest())).collect(Collectors.toList());
//    }

//    public List<Submission> getFrozenSubmits() {
//        return frozenSubmits;
//    }
//
//    public Contest setFrozenSubmits(List<Submission> frozenSubmits) {
//        this.frozenSubmits = frozenSubmits;
//        return this;
//    }
}
