package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "university")
public class University {
    @Id
    @SequenceGenerator(initialValue = 200, name = "universities_seq_id", sequenceName = "universities_seq_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "universities_seq_id")
    @Column(name ="university_id")
    private Long id;

    @Column(name = "university_name")
    private String name;

    @Column(name = "region")
    private String region;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "universityEntity")
//    @JsonBackReference("team-university")
    @JsonIgnore
    private List<Team> teams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public University setName(String name) {
        this.name = name;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public University setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getType() {
        return type;
    }

    public University setType(String type) {
        this.type = type;
        return this;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
