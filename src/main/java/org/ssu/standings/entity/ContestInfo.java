package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.dao.entity.StandingsFile;
import org.ssu.standings.updateobserver.handlers.FileHandler;

import java.util.List;
import java.util.stream.Collectors;

public class ContestInfo {
    @JsonProperty("id")
    private Long contestId;
    @JsonProperty("is_final")
    private Boolean isFinal;
    @JsonProperty("external_files")
    private List<StandingsFile> standingsFiles;


    public ContestInfo() {
    }

    public ContestInfo(Builder builder) {
        this.contestId = builder.contestId;
        this.standingsFiles = builder.standingsFileList;
        this.isFinal = builder.isFinal;
    }


    public Long getContestId() {
        return contestId;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public List<StandingsFile> getStandingsFiles() {
        return standingsFiles;
    }

    @JsonIgnore
    public List<String> getUrlsFromDescriptions() {
        return standingsFiles
                .stream()
                .map(StandingsFile::getLink)
                .collect(Collectors.toList());
    }

    public static final class Builder {
        private Long contestId;
        private Boolean isFinal;
        private List<StandingsFile> standingsFileList;

        public Builder() {
        }

        public Builder(List<StandingsFile> standingsFileList) {
            StandingsFile firstFile = standingsFileList.stream().findFirst().orElseThrow(NullPointerException::new);
            this.contestId = firstFile.getContestId();
            this.standingsFileList = standingsFileList;
            this.isFinal = firstFile.getIsFinal();
        }

        public Builder(Long contestId, Boolean isFinal, List<StandingsFile> standingsFileList) {
            this.contestId = contestId;
            this.isFinal = isFinal;
            this.standingsFileList = standingsFileList;
        }

        public Builder withContestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        public Builder withIsFinal(Boolean isFinal) {
            this.isFinal = isFinal;
            return this;
        }

        public Builder withExternalFileDesriptions(List<StandingsFile> standingsFileList) {
            this.standingsFileList = standingsFileList;
            return this;
        }

        public ContestInfo build() {
            return new ContestInfo(this);
        }
    }
}
