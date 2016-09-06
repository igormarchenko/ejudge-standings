package org.ssu.standings.entity;

public class Task {
    private Long id;
    private String shortName;
    private String longName;

    public Long getId() {
        return id;
    }

    public Task setId(Long id) {
        this.id = id;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public Task setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getLongName() {
        return longName;
    }

    public Task setLongName(String longName) {
        this.longName = longName;
        return this;
    }
}
