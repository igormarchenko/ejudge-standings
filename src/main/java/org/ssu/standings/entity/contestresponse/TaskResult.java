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

public class TaskResult implements Cloneable{
    @JsonIgnore
    private List<SubmissionNode> submissions = new ArrayList<>();

    private TaskResult(Builder builder) {
        submissions = builder.submissions;
    }

    @JsonProperty("tries")
    public Integer submissionCount() {
        List<SubmissionNode> nodes = submissions.stream().filter(submit -> submit.getStatus() != SubmissionStatus.CE).collect(Collectors.toList());

        Map<Integer, SubmissionStatus> collect = IntStream.range(0, nodes.size())
                .boxed()
                .collect(Collectors.toMap(index -> index, index -> nodes.get(index).getStatus()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(item -> item.getKey() + 1, item -> item.getValue()));

        return       collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == SubmissionStatus.OK)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(collect.size());
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
        return (seconds > 0) ? (seconds + 60 - seconds % 60) / 60 : 0L;
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
        return isProblemSolved() ? (Math.max(submissionCount(), 1) - 1) * 20L + getFirstAcceptedTime() : 0L;
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

    @Override
    public TaskResult clone() {
        return new TaskResult.Builder(this).build();
    }


    public static final class Builder {
        private List<SubmissionNode> submissions = new ArrayList<>();

        public Builder() {
        }

        public Builder(TaskResult copy) {
            this.submissions = copy.submissions.stream().map(SubmissionNode::clone).collect(Collectors.toList());
        }

        public Builder withSubmissions(List<SubmissionNode> submissions) {
            this.submissions = submissions;
            return this;
        }

        public TaskResult build() {
            return new TaskResult(this);
        }
    }
}
