package org.ssu.standings.event;

import com.fasterxml.jackson.annotation.*;
import org.ssu.standings.entity.contestresponse.*;

import java.util.*;

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
