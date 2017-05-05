package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.ArrayList;
import java.util.List;

public class TaskResult {
    @JsonProperty("penalty")
    private Long penalty = 0L;
    @JsonProperty("status")
    private SubmissionStatus status = SubmissionStatus.EMPTY;
    @JsonIgnore
    private List<Submission> submissions = new ArrayList<>();

    public TaskResult() {
    }

    @JsonProperty("tries")
    public Integer submissionCount() {
        return submissions.size();
    }

    @JsonProperty("firstAcceptedSubmissionTime")
    public Long getFirstAcceptedTime() {
        return submissions
                .stream()
                .filter(submission -> submission.getStatus() == SubmissionStatus.OK)
                .map(Submission::getTime)
                .sorted()
                .findFirst()
                .orElse(0L);
    }

    public Long getPenalty() {
        return penalty;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public Boolean isProblemSolved() {
        return status == SubmissionStatus.OK;
    }

    public void addNode(SubmissionNode node) {
        SubmissionStatus submissionStatus = SubmissionStatus.valueOf(node.getStatus());
        if (submissionStatus != SubmissionStatus.CE) {
            submissions.add(new Submission(node));
            if (!isProblemSolved() && submissionStatus == SubmissionStatus.OK) {
                penalty = (submissions.size() - 1) * 20 + node.getTime();
                status = submissionStatus;
            }
        }
    }
}
