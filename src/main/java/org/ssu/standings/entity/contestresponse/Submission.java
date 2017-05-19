package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.*;
import org.ssu.standings.entity.*;
import org.ssu.standings.parser.entity.*;

public class Submission {
    @JsonProperty("id")
    private Long id;
    //    @JsonProperty("uuid")
    @JsonIgnore
    private String uuid;
    @JsonProperty("time")
    private Long time;
    @JsonProperty("status")
    private SubmissionStatus status;


    public Submission(SubmissionNode node) {
        id = node.getId();
        uuid = node.getRunUuid();
        time = node.getTime();
        status = SubmissionStatus.valueOf(node.getStatus());
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getTime() {
        return time;
    }

    public SubmissionStatus getStatus() {
        return status;
    }
}