package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return new EqualsBuilder()
                .append(id, task.id)
                .append(shortName, task.shortName)
                .append(longName, task.longName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(shortName)
                .append(longName)
                .toHashCode();
    }
}
