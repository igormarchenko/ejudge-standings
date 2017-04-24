package org.ssu.standings.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.parser.entity.ContestNode;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class Contest {
    @JsonProperty("contest_id")
    private Long contestId;
    @JsonProperty("contest_name")
    private String name;
    @JsonProperty("duration")
    private Long duration;
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("stop_time")
    private LocalDateTime stopTime;
    @JsonProperty("current_time")
    private LocalDateTime currentTime;
    @JsonProperty("fog_time")
    private Long fogTime;
    @JsonProperty("infog_time")
    private Long unfogTime;
    @JsonProperty("results")
    private Map<Long, ParticipantResult> results;

    public Contest(Builder builder) {
        this.contestId = builder.contestId;
        this.name = builder.name;
        this.duration = builder.duration;
        this.startTime = builder.startTime;
        this.stopTime = builder.stopTime;
        this.currentTime = builder.currentTime;
        this.fogTime = builder.fogTime;
        this.unfogTime = builder.unfogTime;
        this.results = builder.results;
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
