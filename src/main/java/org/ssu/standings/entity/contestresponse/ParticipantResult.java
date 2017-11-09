package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.entity.score.ContestType;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ParticipantResult implements Comparator<ParticipantResult>, Comparable<ParticipantResult>, Cloneable {
    @JsonProperty("participant")
    private Participant participant;
    @JsonProperty("results")
    private Map<Long, TaskResult> results = new HashMap<>();
    @JsonIgnore
    private Integer place;
    private ContestType calculator;

    private ParticipantResult(Builder builder) {
        participant = builder.participant;
        results = builder.results;
        place = builder.place;
        calculator = builder.calculator;
    }

    public void pushSubmit(SubmissionNode submit) {
        results.putIfAbsent(submit.getProblemId(), new TaskResult.Builder().withCalculator(calculator.getCalculator()).build());
        results.get(submit.getProblemId()).addSubmission(submit);
    }

    public Map<Long, TaskResult> getResults() {
        return results;
    }

    @JsonProperty("place")
    public Integer getPlace() {
        return place;
    }

    @JsonProperty("penalty")
    public Long getPenalty() {
        return results.values().stream().mapToLong(TaskResult::getPenalty).sum();
    }

    @JsonProperty("solved")
    public Integer solvedProblems() {
        return ((Long) results.values().stream().filter(TaskResult::isProblemSolved).count()).intValue();
    }

    public Participant getParticipant() {
        return participant;
    }

    @Override
    public int compareTo(ParticipantResult o) {
        return compare(this, o);
    }

    @Override
    public int compare(ParticipantResult o1, ParticipantResult o2) {
        return calculator.getComparator().compare(o1, o2);
    }
    @Override
    public ParticipantResult clone() {
        return new ParticipantResult.Builder(this).build();
    }


    public static final class Builder {
        private Participant participant;
        private Map<Long, TaskResult> results  = new HashMap<>();
        private Integer place;
        private ContestType calculator;

        public Builder() {
            this.participant = new Participant.Builder().build();
        }

        public Builder(ParticipantResult copy) {
            this.participant = (copy.participant == null) ? null : copy.participant.clone();
            this.results = copy.getResults().entrySet().stream().collect(Collectors.toMap(item -> item.getKey(), item -> new TaskResult.Builder(item.getValue()).build()));
            this.calculator = copy.calculator;

        }

        public Builder withPlace(Integer place) {
            this.place = place;
            return this;
        }

        public Builder withParticipant(Participant participant) {
            this.participant = participant;
            return this;
        }

        public Builder withCalculator(ContestType calculator) {
            this.calculator = calculator;
            return this;
        }

        public ParticipantResult build() {
            return new ParticipantResult(this);
        }
    }
}
