package org.ssu.standings.entity;

import org.ssu.standings.utils.TeamInUniversityList;

public class Team {
    private Long id;
    private String name;
    private University university;

    public Team(Long id, String name) {
        this.id = id;
        this.name = name.trim();
        this.university = TeamInUniversityList.universityForTeam(name);
    }

    public Long getId() {
        return id;
    }

    public Team setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Team setName(String name) {
        this.name = name.trim();
        return this;
    }

    public University getUniversity() {
        return university;
    }

    public Team setUniversity(University university) {
        this.university = university;
        return this;
    }
}
