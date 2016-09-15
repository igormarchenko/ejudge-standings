package org.ssu.standings.entity;

public class University {
    private String name;
    private String type;

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
}
