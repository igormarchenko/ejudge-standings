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
        try {
            return (SubmissionNode) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
}
