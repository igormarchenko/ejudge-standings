package org.ssu.standings.event;

import org.ssu.standings.entity.contestresponse.*;

import java.util.*;

public class ContestUpdates {
    private Map<Long, ParticipantResult> resultMap;

    public ContestUpdates(Map<Long, ParticipantResult> resultMap) {
        this.resultMap = resultMap;
    }
}
