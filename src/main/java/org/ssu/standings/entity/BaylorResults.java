package org.ssu.standings.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "icpc")
public class BaylorResults {
    @JacksonXmlProperty(localName = "Standing")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<TeamResultsInBaylor> teams;

    public BaylorResults(List<TeamResultsInBaylor> teams) {
        this.teams = teams;
    }
}
