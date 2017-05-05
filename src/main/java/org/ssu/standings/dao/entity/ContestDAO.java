package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
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
    private Boolean isFinal;

    @OneToMany(targetEntity = StandingsFileDAO.class, mappedBy = "contestId")
    @JsonProperty("standingsFiles")
    private List<StandingsFileDAO> standingsFiles;
}
