package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
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
    @JsonIgnore
    private Map<Long, ParticipantResult> results;
    @JsonProperty("tasks")
    private List<Task> tasks;


    public Contest(Builder builder) {
        contestId = builder.contestId;
        name = builder.name;
        duration = builder.duration;
        startTime = builder.startTime;
        stopTime = builder.stopTime;
        currentTime = builder.currentTime;
        fogTime = builder.fogTime;
        unfogTime = builder.unfogTime;
        results = builder.results;
        tasks = builder.tasks;

    }

    @JsonProperty("results")
    public List<ParticipantResult> getResults() {
        //TODO: add lazy update
        return results.values().stream().sorted().collect(Collectors.toList());
    }


    public Map<Long, ParticipantResult> getTeamsResults(Collection<Long> teams) {
        return teams.stream().map(teamId -> results.get(teamId)).collect(Collectors.toMap(team -> team.getParticipant().getId(), team -> team));
    }

    public Contest updateSubmissions(List<SubmissionNode> newSubmissions) {
        newSubmissions.forEach(submit -> {
            results.get(submit.getUserId()).pushSubmit(submit);
        });
        return this;
    }

    public Long getContestId() {
        return contestId;
    }

    public String getName() {
        return name;
    }

    public Long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public Long getFogTime() {
        return fogTime;
    }

    public Long getUnfogTime() {
        return unfogTime;
    }

    public List<Task> getTasks() {
        return tasks;
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


        public Builder(ContestNode contest, Map<String, TeamDAO> teams) {
            Function<String, UniversityDAO> getUniversityForTeam = teamName -> Optional.ofNullable(teams.get(teamName)).map(TeamDAO::getUniversity).orElse(null);
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
                    .map(team -> new Participant.Builder(team, getUniversityForTeam.apply(team.getName())).build())
                    .collect(Collectors.toMap(Participant::getId, team -> new ParticipantResult.Builder().withParticipant(team).build()));

            contest.getSubmissions().forEach(submit -> results.get(submit.getUserId()).pushSubmit(submit));
        }

        public Builder(Contest contest) {
            this.contestId = contest.contestId;
            this.name = contest.name;
            this.duration = contest.duration;
            this.startTime = contest.startTime;
            this.stopTime = contest.stopTime;
            this.currentTime = contest.currentTime;
            this.fogTime = contest.fogTime;
            this.unfogTime = contest.unfogTime;
            this.tasks = new ArrayList<>(contest.tasks);
            this.results = contest.results.entrySet().stream().collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue().clone() ));
        }

        public Contest build() {
            return new Contest(this);
        }
    }
}
