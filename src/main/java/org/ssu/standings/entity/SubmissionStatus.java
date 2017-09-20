package org.ssu.standings.entity;

public enum SubmissionStatus {
    OK("Accepted"),
    WA("Wrong Answer"),
    CE("Compilation Error"),
    TL("Time Limit Exceed"),
    SE("Security Violation"),
    PE("Presentation Error"),
    RT("Runtime Error"),
    ML("Memory Limit Exceed"),
    EMPTY("Empty"),
    FROZEN("Frozen");

    private String status;

    SubmissionStatus(String status) {
        this.status = status;
    }
}
