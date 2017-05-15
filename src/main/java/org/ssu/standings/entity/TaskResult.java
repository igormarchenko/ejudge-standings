package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskResult {
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
        Long seconds = submissions
                .stream()
                .filter(submission -> submission.getStatus() == SubmissionStatus.OK)
                .map(Submission::getTime)
                .sorted()
                .findFirst()
                .orElse(0L);
        return (seconds + 60 - seconds % 60) / 60;
    }

    @JsonProperty("status")
    public SubmissionStatus getStatus() {
        if (submissions.isEmpty()) return SubmissionStatus.EMPTY;
        return submissions.stream()
                .filter(submit -> submit.getStatus() == SubmissionStatus.OK)
                .findAny()
                .map(submit -> SubmissionStatus.OK)
                .orElse(SubmissionStatus.WA);
    }

    @JsonProperty("penalty")
    public Long getPenalty() {
        return isProblemSolved() ? submissionCount() * 20L + getFirstAcceptedTime() : 0L;
    }

    public Boolean isProblemSolved() {
        return getStatus() == SubmissionStatus.OK;
    }

    public void addNode(SubmissionNode node) {
        submissions.add(new Submission(node));
    }
}
