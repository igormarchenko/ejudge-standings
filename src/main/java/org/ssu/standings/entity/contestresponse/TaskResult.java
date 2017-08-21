package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskResult {
    @JsonIgnore
    private List<SubmissionNode> submissions = new ArrayList<>();

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

    @JsonProperty("acceptedTime")
    public Long getFirstAcceptedTime() {
        Long seconds = submissions
                .stream()
                .filter(submission -> submission.getStatus() == SubmissionStatus.OK)
                .map(SubmissionNode::getTime)
                .sorted()
                .findFirst()
                .orElse(0L);
        return (seconds + 60 - seconds % 60) / 60;
    }

    @JsonProperty("status")
    public SubmissionStatus getStatus() {
        if (submissions.isEmpty()) return SubmissionStatus.EMPTY;
        if(isProblemSolved()) return  SubmissionStatus.OK;
        return submissions.stream()
                .filter(submit -> submit.getStatus() != SubmissionStatus.CE)
                .map(SubmissionNode::getStatus)
                .reduce((a, b) -> b)
                .orElse(SubmissionStatus.EMPTY);
    }

    @JsonProperty("penalty")
    public Long getPenalty() {
        return isProblemSolved() ? submissionCount() * 20L + getFirstAcceptedTime() : 0L;
    }

    public List<SubmissionNode> getSubmissions() {
        return submissions;
    }

    @JsonIgnore
    public Boolean isProblemSolved() {
        return submissions.stream()
                .filter(submit -> submit.getStatus() == SubmissionStatus.OK)
                .count() > 0;
    }

    public void addSubmission(SubmissionNode submission) {
        Optional<SubmissionNode> excitingSubmission = submissions.stream().filter(submit -> submit.getRunUuid().equals(submission.getRunUuid())).findFirst();
        if(excitingSubmission.isPresent()) {
            excitingSubmission.get().setStatus(submission.getStatus());
        } else {
            submissions.add(submission);
        }
    }
}
