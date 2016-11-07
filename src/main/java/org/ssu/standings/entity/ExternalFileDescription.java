package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "external_files")
public class ExternalFileDescription {
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

    public ExternalFileDescription() {
    }

    private ExternalFileDescription(Builder builder) {
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

        public Builder(ExternalFileDescription externalFileDescription) {
            this.id = externalFileDescription.getId();
            this.contestId = externalFileDescription.getContestId();
            this.isFinal = externalFileDescription.getIsFinal();
            this.link = externalFileDescription.getLink();
        }

        public Builder withContestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        public Builder withIsFinal(Boolean isFinal) {
            this.isFinal = isFinal;
            return this;
        }
        public ExternalFileDescription build() {
            return new ExternalFileDescription(this);
        }
    }
}
