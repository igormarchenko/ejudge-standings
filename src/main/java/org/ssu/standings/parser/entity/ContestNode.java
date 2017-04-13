package org.ssu.standings.parser.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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
    private List<UserNode> users;

    @JacksonXmlProperty(localName = "problems")
    private List<ProblemNode> problems;

    @JacksonXmlProperty(localName = "languages")
    private List<LanguageNode> languages;

    @JacksonXmlProperty(localName = "runs")
    private List<SubmissionNode> submissions;
}

