package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.ProblemNode;

public class Task {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("longName")
    private String longName;

    public Task(ProblemNode task) {
        id = task.getId();
        shortName = task.getShortName();
        longName = task.getLongName();
    }


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
