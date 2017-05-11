package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.SubmissionNode;

import javax.xml.transform.OutputKeys;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        return IntStream.range(0, submissions.size())
                .boxed()
                .collect(Collectors.toMap(index -> index, index -> submissions.get(index).getStatus()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == SubmissionStatus.OK)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(submissions.size());
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
        return getFirstAcceptedTime() > 0;
    }

    public void addNode(SubmissionNode node) {
        SubmissionStatus submissionStatus = SubmissionStatus.valueOf(node.getStatus());
        if (submissionStatus != SubmissionStatus.CE) {
            submissions.add(new Submission(node));

            if (submissionStatus == SubmissionStatus.OK) {
                penalty = (submissions.size() - 1) * 20 + node.getTime();
            }

            if(isProblemSolved()) {
                status = SubmissionStatus.OK;
            } else {
                status = submissionStatus;
            }
        }
    }
}
