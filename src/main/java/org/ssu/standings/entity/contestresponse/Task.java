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

    private Task(Builder builder) {
        id = builder.id;
        shortName = builder.shortName;
        longName = builder.longName;
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


    public static final class Builder {
        private Long id;
        private String shortName;
        private String longName;

        public Builder() {
        }

        public Builder(Task copy) {
            this.id = copy.id;
            this.shortName = copy.shortName;
            this.longName = copy.longName;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public Builder withLongName(String longName) {
            this.longName = longName;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
