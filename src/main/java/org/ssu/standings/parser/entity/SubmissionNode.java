package org.ssu.standings.parser.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.ssu.standings.entity.SubmissionStatus;

public class SubmissionNode implements Cloneable{
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

    @JacksonXmlProperty(isAttribute = true, localName = "nsec")
    private Long nsec;

    @JacksonXmlProperty(isAttribute = true, localName = "passed_mode")
    private String passedMode;

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

    public Long getUserId() {
        return userId;
    }

    public Long getProblemId() {
        return problemId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public Long getTest() {
        return test;
    }

    public Long getNsec() {
        return nsec;
    }

    public String getPassedMode() {
        return passedMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SubmissionNode that = (SubmissionNode) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(time, that.time)
                .append(runUuid, that.runUuid)
                .append(status, that.status)
                .append(userId, that.userId)
                .append(problemId, that.problemId)
                .append(languageId, that.languageId)
                .append(test, that.test)
                .append(nsec, that.nsec)
                .append(passedMode, that.passedMode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(time)
                .append(runUuid)
                .append(status)
                .append(userId)
                .append(problemId)
                .append(languageId)
                .append(test)
                .append(nsec)
                .append(passedMode)
                .toHashCode();
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public SubmissionNode clone()  {
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

        public Builder() {
        }

        public Builder(SubmissionNode copy) {
            this.id = copy.id;
            this.time = copy.time;
            this.runUuid = copy.runUuid;
            this.status = copy.status;
            this.userId = copy.userId;
            this.problemId = copy.problemId;
            this.languageId = copy.languageId;
            this.test = copy.test;
            this.nsec = copy.nsec;
            this.passedMode = copy.passedMode;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
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

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withProblemId(Long problemId) {
            this.problemId = problemId;
            return this;
        }

        public Builder withLanguageId(Long languageId) {
            this.languageId = languageId;
            return this;
        }

        public Builder withTest(Long test) {
            this.test = test;
            return this;
        }

        public Builder withNsec(Long nsec) {
            this.nsec = nsec;
            return this;
        }

        public Builder withPassedMode(String passedMode) {
            this.passedMode = passedMode;
            return this;
        }

        public SubmissionNode build() {
            return new SubmissionNode(this);
        }
    }
}
