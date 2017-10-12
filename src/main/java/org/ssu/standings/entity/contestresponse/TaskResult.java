package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.entity.score.KirovScoreCalculator;
import org.ssu.standings.entity.score.ScoreCalculator;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskResult implements Cloneable {
    private ScoreCalculator calculator;
    @JsonIgnore
    private List<SubmissionNode> submissions = new ArrayList<>();

    private Integer tries;
    private Long acceptedTime;
    private SubmissionStatus status;
    private Long penalty;

    private TaskResult(Builder builder) {
        submissions = builder.submissions;
        calculator = builder.calculator;
        updateTaskData();
    }

    public TaskResult(ScoreCalculator calculator, List<SubmissionNode> submissions) {
        this.calculator = calculator;
        this.submissions = submissions.stream().filter(submit -> submit.getStatus() != SubmissionStatus.CE).map(SubmissionNode::clone).collect(Collectors.toList());
        updateTaskData();
    }

    public void addSubmission(SubmissionNode submission) {
        if(submission.getStatus() == SubmissionStatus.CE) return;
        submissions.removeIf(submit -> submit.getRunUuid().equals(submission.getRunUuid()));
        submissions.add(submission);
        submissions.sort(Comparator.comparing(SubmissionNode::getTime));
        updateTaskData();
    }

    private void updateTaskData() {
        tries = calculator.tries(submissions);
        acceptedTime = calculator.acceptedTime(submissions);
        status = calculator.status(submissions);
        penalty = calculator.penalty(submissions);
    }

    @JsonProperty("tries")
    public Integer submissionCount() {
        return tries;
    }

    @JsonProperty("acceptedTime")
    public Long getFirstAcceptedTime() {
        return acceptedTime;
    }

    @JsonProperty("status")
    public SubmissionStatus getStatus() {
        return status;
    }

    @JsonProperty("penalty")
    public Long getPenalty() {
        return penalty;
    }

    public List<SubmissionNode> getSubmissions() {
        return submissions;
    }

    @JsonIgnore
    public Boolean isProblemSolved() {
        return calculator.isProblemSolved(submissions);
    }


    @Override
    public TaskResult clone() {
        return new TaskResult.Builder(this).build();
    }

    public static final class Builder {
        private List<SubmissionNode> submissions = new ArrayList<>();
        private ScoreCalculator calculator = new KirovScoreCalculator();

        public Builder() {
        }

        public Builder(TaskResult copy) {
            this.submissions = copy.submissions.stream().filter(submit -> submit.getStatus() != SubmissionStatus.CE).map(SubmissionNode::clone).collect(Collectors.toList());
            this.calculator = copy.calculator;
        }

        public Builder withSubmissions(List<SubmissionNode> submissions) {
            this.submissions = submissions.stream().filter(submit -> submit.getStatus() != SubmissionStatus.CE).map(SubmissionNode::clone).collect(Collectors.toList());
            return this;
        }

        public Builder withCalculator(ScoreCalculator calculator) {
            this.calculator = calculator;
            return this;
        }

        public TaskResult build() {
            return new TaskResult(this);
        }
    }
}
