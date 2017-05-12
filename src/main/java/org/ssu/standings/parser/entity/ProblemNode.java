package org.ssu.standings.parser.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ProblemNode {
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private Long id;

    @JacksonXmlProperty(isAttribute = true, localName = "short_name")
    private String shortName;

    @JacksonXmlProperty(isAttribute = true, localName = "long_name")
    private String longName;

    public Long getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }
}
