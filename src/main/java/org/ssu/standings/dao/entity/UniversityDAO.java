package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "university")
public class UniversityDAO {
    @Id
    @SequenceGenerator(initialValue = 200, name = "universities_seq_id", sequenceName = "universities_seq_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "universities_seq_id")
    @Column(name ="university_id")
    private Long id;

    @Column(name = "university_name")
    @JsonProperty("name")
    private String name;

    @Column(name = "region")
    @JsonProperty("region")
    private String region;

    @Column(name = "type")
    @JsonProperty("type")
    private String type;

    @OneToMany(mappedBy = "university")
//    @JsonBackReference("team-university")
    @JsonIgnore
    private List<TeamDAO> teamDAOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UniversityDAO setName(String name) {
        this.name = name;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public UniversityDAO setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getType() {
        return type;
    }

    public UniversityDAO setType(String type) {
        this.type = type;
        return this;
    }

    public List<TeamDAO> getTeamDAOS() {
        return teamDAOS;
    }

    public void setTeamDAOS(List<TeamDAO> teamDAOS) {
        this.teamDAOS = teamDAOS;
    }
}
