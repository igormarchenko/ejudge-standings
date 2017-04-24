package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.ParticipantNode;

import java.util.Optional;

public class Participant {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    public Participant(Builder builder) {
        id = builder.id;
        name = builder.name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static final class Builder {
        private Long id;
        private String name;

        public Builder(ParticipantNode user) {
            id = user.getId();
            name = Optional.ofNullable(user.getName())
                    .filter(teamName -> !teamName.isEmpty())
                    .orElse(String.format("team%d", id));
        }

        public Participant build() {
            return new Participant(this);
        }
    }
}
