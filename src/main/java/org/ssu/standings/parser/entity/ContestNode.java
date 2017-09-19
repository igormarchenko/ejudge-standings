package org.ssu.standings.parser.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ContestNode {
    @JacksonXmlProperty(isAttribute = true, localName = "contest_id")
    private Long contestId;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(isAttribute = true, localName = "duration")
    private Long duration;

    @JacksonXmlProperty(isAttribute = true, localName = "start_time")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime startTime;

    @JacksonXmlProperty(isAttribute = true, localName = "stop_time")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime stopTime;

    @JacksonXmlProperty(isAttribute = true, localName = "current_time")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime currentTime;

    @JacksonXmlProperty(isAttribute = true, localName = "fog_time")
    private Long fogTime;

    @JacksonXmlProperty(isAttribute = true, localName = "unfog_time")
    private Long unfogTime;

    @JacksonXmlProperty(localName = "users")
    private List<ParticipantNode> participants;

    @JacksonXmlProperty(localName = "problems")
    private List<ProblemNode> problems;

    @JacksonXmlProperty(localName = "languages")
    private List<LanguageNode> languages;

    @JacksonXmlProperty(localName = "runs")
    private List<SubmissionNode> submissions;


    public Long getContestId() {
        return contestId;
    }

    public String getName() {
        return name;
    }

    public Long getDuration() {
        if(duration == null && stopTime != null)
            return ChronoUnit.SECONDS.between(startTime, stopTime);
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getStopTime() {
        if(stopTime== null && duration != null)
            return startTime.plusSeconds(duration);
        return stopTime;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public Long getFogTime() {
        return fogTime;
    }

    public Long getUnfogTime() {
        return unfogTime;
    }

    public List<ParticipantNode> getParticipants() {
        return participants;
    }

    public List<ProblemNode> getProblems() {
        return problems;
    }

    public List<LanguageNode> getLanguages() {
        return languages;
    }

    public List<SubmissionNode> getSubmissions() {
        return submissions;
    }
}

