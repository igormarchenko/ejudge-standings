package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.parser.entity.ParticipantNode;

import java.util.Optional;

public class Participant {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("university")
    private UniversityDAO university;

    public Participant(Builder builder) {
        id = builder.id;
        name = builder.name;
        university = builder.university;
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
        private UniversityDAO university;

        public Builder(ParticipantNode user, UniversityDAO university) {
            id = user.getId();
            name = Optional.ofNullable(user.getName())
                    .filter(teamName -> !teamName.isEmpty())
                    .orElse(String.format("team%d", id));
            this.university = university;
        }

        public Participant build() {
            return new Participant(this);
        }
    }
}
