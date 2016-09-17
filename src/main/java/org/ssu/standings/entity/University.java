package org.ssu.standings.entity;

public class University {
    private String name;
    private String type;
    private String region;

    public String getName() {
        return name;
    }

    public University setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public University setType(String type) {
        this.type = type;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public University setRegion(String region) {
        this.region = region;
        return this;
    }
}
