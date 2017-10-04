package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "university")
public class UniversityDAO implements Cloneable {
    @Id
    @SequenceGenerator(initialValue = 200, name = "universities_seq_id", sequenceName = "universities_seq_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "universities_seq_id")
    @Column(name = "university_id")
    @JsonProperty("id")
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
    @JsonIgnore
    private List<TeamDAO> teamDAOS;

    public UniversityDAO() {
    }

    private UniversityDAO(Builder builder) {
        id = builder.id;
        name = builder.name;
        region = builder.region;
        type = builder.type;
        teamDAOS = builder.teamDAOS;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getType() {
        return type;
    }

    public List<TeamDAO> getTeamDAOS() {
        return teamDAOS;
    }

    @Override
    public UniversityDAO clone() {
        return new Builder().withId(id).withName(name).withRegion(region).withType(type).build();
    }


    public static final class Builder {
        private Long id;
        private String name;
        private String region;
        private String type;
        private List<TeamDAO> teamDAOS;

        public Builder() {
        }

        public Builder(UniversityDAO copy) {
            this.id = copy.id;
            this.name = copy.name;
            this.region = copy.region;
            this.type = copy.type;
            this.teamDAOS = copy.teamDAOS;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRegion(String region) {
            this.region = region;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withTeamDAOS(List<TeamDAO> teamDAOS) {
            this.teamDAOS = teamDAOS;
            return this;
        }

        public UniversityDAO build() {
            return new UniversityDAO(this);
        }
    }
}
