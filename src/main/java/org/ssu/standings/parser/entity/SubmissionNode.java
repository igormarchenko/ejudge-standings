package org.ssu.standings.parser.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.ssu.standings.entity.SubmissionStatus;

public class SubmissionNode implements Cloneable {
    @JacksonXmlProperty(isAttribute = true, localName = "run_id")
    private Long id;

    @JacksonXmlProperty(isAttribute = true, localName = "time")
    private Long time;

    @JacksonXmlProperty(isAttribute = true, localName = "run_uuid")
    private String runUuid;

    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private SubmissionStatus status;

    @JacksonXmlProperty(isAttribute = true, localName = "user_id")
    private Long userId;

    @JacksonXmlProperty(isAttribute = true, localName = "prob_id")
    private Long problemId;

    @JacksonXmlProperty(isAttribute = true, localName = "lang_id")
    private Long languageId;

    @JacksonXmlProperty(isAttribute = true, localName = "test")
    private Long test;

    @JacksonXmlProperty(isAttribute = true, localName = "score")
    private Long score;

    @JacksonXmlProperty(isAttribute = true, localName = "nsec")
    private Long nsec;

    @JacksonXmlProperty(isAttribute = true, localName = "passed_mode")
    private String passedMode;

    private String username;

    public SubmissionNode() {
    }

    private SubmissionNode(Builder builder) {
        id = builder.id;
        time = builder.time;
        runUuid = builder.runUuid;
        setStatus(builder.status);
        userId = builder.userId;
        problemId = builder.problemId;
        languageId = builder.languageId;
        test = builder.test;
        nsec = builder.nsec;
        passedMode = builder.passedMode;
        username = builder.username;
        score = builder.score;
    }

    public Long getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public String getRunUuid() {
        return runUuid;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public Long getTest() {
        return test;
    }

    public Long getScore() {
        return score;
    }

    public Long getNsec() {
        return nsec;
    }

    public String getPassedMode() {
        return passedMode;
    }

    public String getUsername() {
        return username;
    }

    public SubmissionNode clone() {
        return new SubmissionNode.Builder(this).build();
    }


    public static final class Builder {
        private Long id;
        private Long time;
        private String runUuid;
        private SubmissionStatus status;
        private Long userId;
        private Long problemId;
        private Long languageId;
        private Long test;
        private Long nsec;
        private String passedMode;
        private String username;
        private Long score;

        public Builder() {
        }

        public Builder(SubmissionNode copy) {
            this.id = copy.getId();
            this.time = copy.getTime();
            this.runUuid = copy.getRunUuid();
            this.status = copy.getStatus();
            this.userId = copy.getUserId();
            this.problemId = copy.getProblemId();
            this.languageId = copy.getLanguageId();
            this.test = copy.getTest();
            this.nsec = copy.getNsec();
            this.passedMode = copy.getPassedMode();
            this.username = copy.getUsername();
            this.score = copy.getScore();
        }

        public Builder withTime(Long time) {
            this.time = time;
            return this;
        }

        public Builder withRunUuid(String runUuid) {
            this.runUuid = runUuid;
            return this;
        }

        public Builder withStatus(SubmissionStatus status) {
            this.status = status;
            return this;
        }

        public Builder withProblemId(Long problemId) {
            this.problemId = problemId;
            return this;
        }

        public Builder withUsername(String name) {
            this.username = name;
            return this;
        }

        public Builder withScore(Long score) {
            this.score = score;
            return this;
        }

        public SubmissionNode build() {
            return new SubmissionNode(this);
        }
    }
}
