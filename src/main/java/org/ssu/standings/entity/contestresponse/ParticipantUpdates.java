package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParticipantUpdates {
    @JsonProperty("id")
    private Long teamId;
    @JsonProperty("result")
    private ParticipantResult result;
    @JsonProperty("previousPlace")
    private Integer previousPlace;
    @JsonProperty("currentPlace")
    private Integer currentPlace;

    public ParticipantUpdates(Long teamId, ParticipantResult result, Integer previousPlace, Integer currentPlace) {
        this.teamId = teamId;
        this.result = result;
        this.previousPlace = previousPlace;
        this.currentPlace = currentPlace;
    }

    public Long getTeamId() {
        return teamId;
    }

    public ParticipantResult getResult() {
        return result;
    }

    public Integer getPreviousPlace() {
        return previousPlace;
    }

    public Integer getCurrentPlace() {
        return currentPlace;
    }
}
