package org.ssu.standings.entity;


import org.ssu.standings.parser.entity.LanguageNode;

public class Language {
    private Long id;
    private String shortName;
    private String longName;

    public Language(Builder builder) {
        this.id = builder.id;
        this.shortName = builder.shortName;
        this.longName = builder.longName;
    }

    public Long getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public static final class Builder {
        private Long id;
        private String shortName;
        private String longName;

        public Builder(LanguageNode language) {
            id = language.getId();
            shortName = language.getShortName();
            longName = language.getLongName();
        }

        public Language build() {
            return new Language(this);
        }
    }
}
