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

    private StandingsFileDAO(Builder builder) {
        id = builder.id;
        link = builder.link;
        contestId = builder.contestId;
        isFrozen = builder.isFrozen;
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
                .append(isFrozen, that.isFrozen)
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


    public static final class Builder {
        private Long id;
        private String link;
        private Long contestId;
        private Boolean isFrozen;

        public Builder() {
        }

        public Builder(StandingsFileDAO copy) {
            this.id = copy.id;
            this.link = copy.link;
            this.contestId = copy.contestId;
            this.isFrozen = copy.isFrozen;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withLink(String link) {
            this.link = link;
            return this;
        }

        public Builder withContestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        public Builder withIsFrozen(Boolean isFrozen) {
            this.isFrozen = isFrozen;
            return this;
        }

        public StandingsFileDAO build() {
            return new StandingsFileDAO(this);
        }
    }
}
