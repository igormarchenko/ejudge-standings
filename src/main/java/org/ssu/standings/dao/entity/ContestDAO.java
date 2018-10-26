package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contest")
public class ContestDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "is_final")
    @JsonProperty("is_final")
    private Boolean isFinal;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "contest_id")
    @JsonProperty("standingsFiles")
    private List<StandingsFileDAO> standingsFiles = new ArrayList<>();

    public ContestDAO() {
    }

    private ContestDAO(Builder builder) {
        id = builder.id;
        name = builder.name;
        standingsFiles = builder.standingsFiles;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StandingsFileDAO> getStandingsFiles() {
        return standingsFiles;
    }

    public Boolean getFinal() {
        return isFinal;
    }

    public static final class Builder {
        private Long id;
        private String name;
        private List<StandingsFileDAO> standingsFiles;
        private Boolean isFinal;

        public Builder() {
        }

        public Builder(ContestDAO copy) {
            this.id = copy.id;
            this.name = copy.name;
            this.isFinal = copy.isFinal;
            this.standingsFiles = copy.standingsFiles;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withStandingsFiles(List<StandingsFileDAO> standingsFiles) {
            this.standingsFiles = standingsFiles;
            return this;
        }

        public Builder withIsFinal(Boolean isFinal) {
            this.isFinal = isFinal;
            return this;
        }
        public ContestDAO build() {
            return new ContestDAO(this);
        }
    }
}
