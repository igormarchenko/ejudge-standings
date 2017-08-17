package org.ssu.standings.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.entity.contestresponse.ParticipantUpdates;

import java.util.Map;

public class ContestUpdates {
    @JsonIgnore
    private Long contestId;
    @JsonProperty("updates")
    private Map<Long, ParticipantUpdates> updatedResults;

    public ContestUpdates(Long contestId, Map<Long, ParticipantUpdates> updatedResults) {
        this.contestId = contestId;
        this.updatedResults = updatedResults;
    }

    @JsonIgnore
    public Long getContestId() {
        return contestId;
    }
}
