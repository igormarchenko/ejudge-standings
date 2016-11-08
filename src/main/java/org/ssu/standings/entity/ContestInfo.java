package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class ContestInfo {
    @JsonProperty("id")
    private Long contestId;
    @JsonProperty("is_final")
    private Boolean isFinal;
    @JsonProperty("external_files")
    private List<ExternalFileDescription> externalFileDescriptionList;

    public ContestInfo() {
    }

    public ContestInfo(Builder builder) {
        this.contestId = builder.contestId;
        this.externalFileDescriptionList = builder.externalFileDescriptionList;
        this.isFinal = builder.isFinal;
    }


    public Long getContestId() {
        return contestId;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public List<ExternalFileDescription> getExternalFileDescriptionList() {
        return externalFileDescriptionList;
    }

    @JsonIgnore
    public List<String> getUrlsFromDescriptions() {
        return externalFileDescriptionList
                .stream()
                .map(ExternalFileDescription::getLink)
                .collect(Collectors.toList());
    }

    public static final class Builder {
        private Long contestId;
        private Boolean isFinal;
        private List<ExternalFileDescription> externalFileDescriptionList;

        public Builder() {
        }

        public Builder(List<ExternalFileDescription> externalFileDescriptionList) {
            ExternalFileDescription firstFile = externalFileDescriptionList.stream().findFirst().orElseThrow(NullPointerException::new);
            this.contestId = firstFile.getContestId();
            this.externalFileDescriptionList = externalFileDescriptionList;
            this.isFinal = firstFile.getIsFinal();
        }

        public Builder(Long contestId, Boolean isFinal, List<ExternalFileDescription> externalFileDescriptionList) {
            this.contestId = contestId;
            this.isFinal = isFinal;
            this.externalFileDescriptionList = externalFileDescriptionList;
        }

        public Builder withContestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        public Builder withIsFinal(Boolean isFinal) {
            this.isFinal = isFinal;
            return this;
        }

        public Builder withExternalFileDesriptions(List<ExternalFileDescription> externalFileDescriptionList) {
            this.externalFileDescriptionList = externalFileDescriptionList;
            return this;
        }

        public ContestInfo build() {
            return new ContestInfo(this);
        }
    }
}
