package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ParticipantResult implements Comparator<ParticipantResult>, Comparable<ParticipantResult> {
    @JsonProperty("participant")
    private Participant participant;
    @JsonProperty("results")
    private Map<Long, TaskResult> results = new HashMap<>();

    public ParticipantResult(Participant participant) {
        this.participant = participant;
    }

    public void pushSubmit(SubmissionNode submit) {
        results.putIfAbsent(submit.getProblemId(), new TaskResult());
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
    public Long solvedProblems() {
        return results.values().stream().filter(TaskResult::isProblemSolved).count();
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
}
