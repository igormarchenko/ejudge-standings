package org.ssu.standings.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.entity.contestresponse.ParticipantResult;

import java.util.Map;

public class ContestUpdates {
    @JsonIgnore
    private Long contestId;
    @JsonProperty("updates")
    private Map<Long, ParticipantResult> resultMap;

    public ContestUpdates(Long contestId, Map<Long, ParticipantResult> resultMap) {
        this.contestId = contestId;
        this.resultMap = resultMap;
    }

    @JsonIgnore
    public Long getContestId() {
        return contestId;
    }
}
