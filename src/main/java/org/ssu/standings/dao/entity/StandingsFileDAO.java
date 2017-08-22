package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "standings_files")
public class StandingsFileDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "link")
    @JsonProperty("link")
    private String link;

    @Column(name = "contest_id")
    @JsonIgnore
    private Long contestId;

    @Column(name = "is_frozen", nullable = false)
    @JsonIgnore
    private Boolean isFrozen;

    public StandingsFileDAO() {
    }

    public Long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public Long getContestId() {
        return contestId;
    }

    public Boolean getFrozen() {
        return isFrozen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StandingsFileDAO that = (StandingsFileDAO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(link, that.link)
                .append(contestId, that.contestId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(link)
                .append(contestId)
                .toHashCode();
    }
}
