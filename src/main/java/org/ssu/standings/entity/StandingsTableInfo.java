package org.ssu.standings.entity;

public class StandingsTableInfo {
    private String link;
    private Long contestId;
    private Boolean isFinal;

    public String getLink() {
        return link;
    }

    public StandingsTableInfo setLink(String link) {
        this.link = link;
        return this;
    }

    public Long getContestId() {
        return contestId;
    }

    public StandingsTableInfo setContestId(Long contestId) {
        this.contestId = contestId;
        return this;
    }

    public Boolean getFinal() {
        return isFinal;
    }

    public StandingsTableInfo setFinal(Boolean aFinal) {
        isFinal = aFinal;
        return this;
    }
}
