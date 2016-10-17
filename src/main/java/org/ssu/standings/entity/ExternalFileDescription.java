package org.ssu.standings.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "external_files")
public class ExternalFileDescription {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "link")
    private String link;

    @Column(name = "contest_id")
    private Long contestId;

    @Column(name = "is_final")
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

    public Boolean getFinal() {
        return isFinal;
    }
}
