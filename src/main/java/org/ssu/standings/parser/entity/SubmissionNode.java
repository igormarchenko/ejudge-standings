package org.ssu.standings.parser.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SubmissionNode {
    @JacksonXmlProperty(isAttribute = true, localName = "run_id")
    private Long id;

    @JacksonXmlProperty(isAttribute = true, localName = "time")
    private Long time;

    @JacksonXmlProperty(isAttribute = true, localName = "run_uuid")
    private String runUuid;

    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private String status;

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
}
