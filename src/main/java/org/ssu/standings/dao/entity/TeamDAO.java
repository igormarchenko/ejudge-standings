package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "teams")
public class TeamDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    @JsonProperty("id")
    private Long id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    @JsonProperty("universityDAO")
    private UniversityDAO university;

    @JsonProperty("contest_team_id")
    private transient Long teamIdInContest;

    public Long getTeamIdInContest() {
        return teamIdInContest;
    }

    public TeamDAO setTeamIdInContest(Long teamIdInContest) {
        this.teamIdInContest = teamIdInContest;
        return this;
    }

    public Long getId() {
        return id;
    }

    public TeamDAO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TeamDAO setName(String name) {
        this.name = name;
        return this;
    }

    public UniversityDAO getUniversity() {
        return university;
    }

    public TeamDAO setUniversity(UniversityDAO university) {
        this.university = university;
        return this;
    }
}
