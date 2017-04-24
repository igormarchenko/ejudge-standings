package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.dao.entity.StandingsFileDAO;

import java.util.List;
import java.util.stream.Collectors;

public class ContestInfo {
    @JsonProperty("id")
    private Long contestId;
    @JsonProperty("is_final")
    private Boolean isFinal;
    @JsonProperty("external_files")
    private List<StandingsFileDAO> standingsFileDAOS;


    public ContestInfo() {
    }

    public ContestInfo(Builder builder) {
        this.contestId = builder.contestId;
        this.standingsFileDAOS = builder.standingsFileDAOList;
        this.isFinal = builder.isFinal;
    }


    public Long getContestId() {
        return contestId;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public List<StandingsFileDAO> getStandingsFileDAOS() {
        return standingsFileDAOS;
    }

    @JsonIgnore
    public List<String> getUrlsFromDescriptions() {
        return standingsFileDAOS
                .stream()
                .map(StandingsFileDAO::getLink)
                .collect(Collectors.toList());
    }

    public static final class Builder {
        private Long contestId;
        private Boolean isFinal;
        private List<StandingsFileDAO> standingsFileDAOList;

        public Builder() {
        }

        public Builder(List<StandingsFileDAO> standingsFileDAOList) {
            StandingsFileDAO firstFile = standingsFileDAOList.stream().findFirst().orElseThrow(NullPointerException::new);
            this.contestId = firstFile.getContestId();
            this.standingsFileDAOList = standingsFileDAOList;
            this.isFinal = firstFile.getIsFinal();
        }

        public Builder(Long contestId, Boolean isFinal, List<StandingsFileDAO> standingsFileDAOList) {
            this.contestId = contestId;
            this.isFinal = isFinal;
            this.standingsFileDAOList = standingsFileDAOList;
        }

        public Builder withContestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        public Builder withIsFinal(Boolean isFinal) {
            this.isFinal = isFinal;
            return this;
        }

        public Builder withExternalFileDesriptions(List<StandingsFileDAO> standingsFileDAOList) {
            this.standingsFileDAOList = standingsFileDAOList;
            return this;
        }

        public ContestInfo build() {
            return new ContestInfo(this);
        }
    }
}
