package org.ssu.standings.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "standings_files")
public class StandingsFile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "link")
    @JsonProperty("link")
    private String link;

    @Column(name = "contest_id")
    @JsonProperty("contest_id")
    private Long contestId;

    @Column(name = "is_final")
    @JsonProperty("is_final")
    private Boolean isFinal;

    public Long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public Long getContestId() {
        return contestId;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public StandingsFile() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StandingsFile that = (StandingsFile) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(link, that.link)
                .append(contestId, that.contestId)
                .append(isFinal, that.isFinal)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(link)
                .append(contestId)
                .append(isFinal)
                .toHashCode();
    }

    private StandingsFile(Builder builder) {
        this.id = builder.id;
        this.isFinal = builder.isFinal;
        this.link = builder.link;
        this.contestId = builder.contestId;
    }

    public static final class Builder{
        private Long id;
        private String link;
        private Long contestId;
        private Boolean isFinal;

        public Builder(StandingsFile standingsFile) {
            this.id = standingsFile.getId();
            this.contestId = standingsFile.getContestId();
            this.isFinal = standingsFile.getIsFinal();
            this.link = standingsFile.getLink();
        }

        public Builder withContestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        public Builder withIsFinal(Boolean isFinal) {
            this.isFinal = isFinal;
            return this;
        }
        public StandingsFile build() {
            return new StandingsFile(this);
        }
    }
}