package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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


    @ManyToOne
    @JoinColumn(name = "contest_id")
    @JsonProperty("contest_id")
    @JsonBackReference
    private ContestDAO contest;

    @Column(name = "is_frozen", nullable = false)
    @JsonProperty("frozen")
    private Boolean isFrozen;

    public StandingsFileDAO() {
    }

    private StandingsFileDAO(Builder builder) {
        id = builder.id;
        link = builder.link;
        contest = builder.contest;
        isFrozen = builder.isFrozen;
    }

    public Long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public Long getContestId() {
        return contest.getId();
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
                .append(contest, that.contest)
                .append(isFrozen, that.isFrozen)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(link)
                .append(contest)
                .toHashCode();
    }


    public static final class Builder {
        private Long id;
        private String link;
        private ContestDAO contest;
        private Boolean isFrozen;

        public Builder() {
        }

        public Builder(StandingsFileDAO copy) {
            this.id = copy.id;
            this.link = copy.link;
            this.contest = copy.contest;
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

        public Builder withContestId(ContestDAO contest) {
            this.contest = contest;
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
