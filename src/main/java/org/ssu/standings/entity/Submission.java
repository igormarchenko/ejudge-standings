package org.ssu.standings.entity;

public class Submission {
    private Long runId;
    private Long time;
    private String status;
    private Long userId;
    private Long problemId;
    private Long languageId;

    public Long getRunId() {
        return runId;
    }

    public Submission setRunId(Long runId) {
        this.runId = runId;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public Submission setTime(Long time) {
        this.time = time;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Submission setStatus(String status) {
        this.status = status;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Submission setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getProblemId() {
        return problemId;
    }

    public Submission setProblemId(Long problemId) {
        this.problemId = problemId;
        return this;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public Submission setLanguageId(Long languageId) {
        this.languageId = languageId;
        return this;
    }
}