package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import javax.persistence.*;

@Entity
@Table(name = "teams")
public class TeamDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "name")
    @JsonProperty(value = "name", required = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "university_id")
    @JsonProperty("university")
    private UniversityDAO university;

    public TeamDAO() {
    }

    private TeamDAO(Builder builder) {
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

    public UniversityDAO getUniversity() {
        return university;
    }


    public static final class Builder {
        private Long id;
        private String name;
        private UniversityDAO university;
        private Long teamIdInContest;

        public Builder() {
        }

        public Builder(TeamDAO copy) {
            this.id = copy.id;
            this.name = copy.name;
            this.university = copy.university;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withUniversity(UniversityDAO university) {
            this.university = university;
            return this;
        }

        public TeamDAO build() {
            return new TeamDAO(this);
        }
    }
}
