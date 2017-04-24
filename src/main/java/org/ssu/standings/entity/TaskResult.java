package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.ArrayList;
import java.util.List;

public class TaskResult {
    @JsonProperty("penalty")
    private Long penalty = 0L;
    @JsonProperty("status")
    private SubmissionStatus status = SubmissionStatus.EMPTY;
    @JsonProperty("submissions")
    private List<Submission> submissions = new ArrayList<>();

    public TaskResult() {

    }

    public Long getPenalty() {
        return penalty;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void addNode(SubmissionNode node) {
        SubmissionStatus submissionStatus = SubmissionStatus.valueOf(node.getStatus());
        if(status.equals(SubmissionStatus.OK)) {
            submissions.add(new Submission(node));
        } else {
            if(!submissionStatus.equals(SubmissionStatus.CE)) {
                submissions.add(new Submission(node));
                if(submissionStatus.equals(SubmissionStatus.OK)) {
                    penalty = (submissions.size() - 1) * 20 + node.getTime();
                }
                status = submissionStatus;
            }
        }
    }
}
