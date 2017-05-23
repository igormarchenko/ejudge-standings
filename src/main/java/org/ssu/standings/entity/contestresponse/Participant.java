package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    public Participant(ParticipantNode user, UniversityDAO university) {
        id = user.getId();
        name = Optional.ofNullable(user.getName())
                .filter(teamName -> !teamName.isEmpty())
                .orElse(String.format("team%d", id));
        this.university = university;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
