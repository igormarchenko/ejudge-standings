package org.ssu.standings.parser.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class LanguageNode {
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private Long id;

    @JacksonXmlProperty(isAttribute = true, localName = "short_name")
    private String shortName;

    @JacksonXmlProperty(isAttribute = true, localName = "long_name")
    private String longName;
}
