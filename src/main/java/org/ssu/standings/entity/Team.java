package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    @JsonIgnore
    private Long id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    @JsonProperty("university")
    @JsonManagedReference("team-university")
    private University universityEntity;

    @JsonProperty("id")
    private transient Long teamIdInContest;

    public Long getTeamIdInContest() {
        return teamIdInContest;
    }

    public Team setTeamIdInContest(Long teamIdInContest) {
        this.teamIdInContest = teamIdInContest;
        return this;
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
        this.name = name;
        return this;
    }

    public University getUniversityEntity() {
        return universityEntity;
    }

    public Team setUniversityEntity(University universityEntity) {
        this.universityEntity = universityEntity;
        return this;
    }
}
