package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ParticipantResult implements Comparator<ParticipantResult>, Comparable<ParticipantResult>, Cloneable {
    @JsonProperty("participant")
    private Participant participant;
    @JsonProperty("results")
    private Map<Long, TaskResult> results = new HashMap<>();

    private ParticipantResult(Builder builder) {
        participant = builder.participant;
        results = builder.results;
    }

    public void pushSubmit(SubmissionNode submit) {
        results.putIfAbsent(submit.getProblemId(), new TaskResult.Builder().build());
        results.get(submit.getProblemId()).addSubmission(submit);
    }

    public Map<Long, TaskResult> getResults() {
        return results;
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
        if (!o1.solvedProblems().equals(o2.solvedProblems()))
            return Long.valueOf(o2.solvedProblems() - o1.solvedProblems()).intValue();
        else
            return Long.valueOf(o1.getPenalty() - o2.getPenalty()).intValue();
    }
    @Override
    public ParticipantResult clone() {
        return new ParticipantResult.Builder(this).build();
    }


    public static final class Builder {
        private Participant participant;
        private Map<Long, TaskResult> results  = new HashMap<>();

        public Builder() {
        }

        public Builder(ParticipantResult copy) {
            this.participant = (copy.participant == null) ? null : copy.participant.clone();
            this.results.putAll(copy.getResults());
        }

        public Builder withParticipant(Participant participant) {
            this.participant = participant;
            return this;
        }

        public ParticipantResult build() {
            return new ParticipantResult(this);
        }
    }
}
