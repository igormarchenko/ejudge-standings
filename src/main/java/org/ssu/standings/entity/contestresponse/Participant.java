package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.parser.entity.ParticipantNode;

import java.util.Optional;

public class Participant implements Cloneable{
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("university")
    private UniversityDAO university;

    public Participant(ParticipantNode user, UniversityDAO university) {

        id = user.getId();
        name = Optional.ofNullable(user.getName())
                .filter(teamName -> !teamName.isEmpty())
                .orElse(String.format("team%d", id));
        this.university = university;
    }

    private Participant(Builder builder) {
        setId(builder.id);
        name = builder.name;
        university = builder.university;
    }

    public Long getId() {
        return id;
    }

    public Participant setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public Participant clone() {
        return new Participant.Builder().withId(id).withName(name).withUniversity(university.clone()).build();
    }


    public static final class Builder {
        private Long id;
        private String name;
        private UniversityDAO university;

        public Builder() {
        }

        public Builder withId(Long val) {
            id = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withUniversity(UniversityDAO val) {
            university = val;
            return this;
        }

        public Participant build() {
            return new Participant(this);
        }
    }
}
