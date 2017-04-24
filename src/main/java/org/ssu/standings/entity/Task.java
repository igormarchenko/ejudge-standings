package org.ssu.standings.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.ssu.standings.parser.entity.ProblemNode;

public class Task {
    private Long id;
    private String shortName;
    private String longName;

    public Task(Builder builder) {
        this.id = builder.id;
        this.shortName = builder.shortName;
        this.longName = builder.longName;
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

    public static final class Builder {
        private Long id;
        private String shortName;
        private String longName;

        public Builder(ProblemNode task) {
            id = task.getId();
            shortName = task.getShortName();
            longName = task.getLongName();
        }

        public Task build() {
            return new Task(this);
        }
    }
}
