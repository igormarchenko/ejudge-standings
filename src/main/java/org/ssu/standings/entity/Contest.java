package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.ContestNode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Contest {
    @JsonProperty("id")
    private Long contestId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("duration")
    private Long duration;
    @JsonProperty("startTime")
    private LocalDateTime startTime;
    @JsonProperty("stopTime")
    private LocalDateTime stopTime;
    @JsonProperty("currentTime")
    private LocalDateTime currentTime;
    @JsonProperty("fogTime")
    private Long fogTime;
    @JsonProperty("unfogTime")
    private Long unfogTime;
    @JsonProperty("results")
    private List<ParticipantResult> results;

    @JsonProperty("tasks")
    private List<Task> tasks;

    public Contest(Builder builder) {
        this.contestId = builder.contestId;
        this.name = builder.name;
        this.duration = builder.duration;
        this.startTime = builder.startTime;
        this.stopTime = builder.stopTime;
        this.currentTime = builder.currentTime;
        this.fogTime = builder.fogTime;
        this.unfogTime = builder.unfogTime;
        this.results = builder.results.values().stream().sorted().collect(Collectors.toList());
        this.tasks = builder.tasks;
    }

    public static final class Builder {
        private Long contestId;
        private String name;
        private Long duration;
        private LocalDateTime startTime;
        private LocalDateTime stopTime;
        private LocalDateTime currentTime;
        private Long fogTime;
        private Long unfogTime;
        private List<Task> tasks;
        private Map<Long, ParticipantResult> results;

        public Builder(ContestNode contest) {
            contestId = contest.getContestId();
            name = contest.getName();
            duration = contest.getDuration();
            startTime = contest.getStartTime();
            stopTime = contest.getStopTime();
            currentTime = contest.getCurrentTime();
            fogTime = contest.getFogTime();
            unfogTime = contest.getUnfogTime();
            tasks = contest.getProblems().stream().map(Task::new).collect(Collectors.toList());
            results = contest.getParticipants()
                    .stream()
                    .map(team -> new Participant.Builder(team).build())
                    .collect(Collectors.toMap(Participant::getId, ParticipantResult::new));

            contest.getSubmissions().forEach(submit -> results.get(submit.getUserId()).pushSubmit(submit));
        }

        public Contest build() {
            return new Contest(this);
        }
    }
}
