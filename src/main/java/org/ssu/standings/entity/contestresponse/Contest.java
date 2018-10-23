package org.ssu.standings.entity.contestresponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.entity.score.ContestType;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private Map<String, ParticipantResult> results;
    @JsonProperty("tasks")
    private List<Task> tasks;

    @JsonProperty("contestType")
    private ContestType contestType;

    public Contest(Builder builder) {
        contestId = builder.contestId;
        name = builder.name;
        duration = builder.duration;
        startTime = builder.startTime;
        stopTime = builder.stopTime;
        currentTime = builder.currentTime;
        fogTime = Optional.ofNullable(builder.fogTime).orElse(3600L);
        unfogTime = Optional.ofNullable(builder.unfogTime).orElse(7200L);
        results = builder.results;
        tasks = builder.tasks;
        contestType = builder.contestType;
    }

    @JsonProperty("results")
    public List<ParticipantResult> getResults() {
        List<ParticipantResult> results = this.results.values().stream().sorted().collect(Collectors.toList());
        return IntStream.range(0, results.size())
                .mapToObj(index -> new ParticipantResult.Builder(results.get(index)).withCalculator(contestType).withPlace(index + 1).build())
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public Map<String, ParticipantResult> getTeamsResults(Collection<String> teams) {
        return teams.stream().map(teamId -> results.get(teamId)).collect(Collectors.toMap(team -> team.getParticipant().getName(), team -> team));
    }

    @JsonIgnore
    public List<SubmissionNode> getSubmissions() {
        return results.values().stream()
                .flatMap(participantResult -> participantResult.getResults().values().stream())
                .flatMap(taskResult -> taskResult.getSubmissions().stream())
                .collect(Collectors.toList());
    }

    public Contest updateSubmissions(List<SubmissionNode> newSubmissions) {
        newSubmissions.forEach(submit -> {
            results.get(submit.getUsername()).pushSubmit(submit);
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
        private Map<String, ParticipantResult> results = new HashMap<>();
        private ContestType contestType;

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
            contest.getParticipants().forEach(team -> results.put(team.getName(), new ParticipantResult.Builder()
                    .withCalculator(getCalculator())
                    .withParticipant(new Participant
                            .Builder()
                            .withId(team.getId())
                            .withName(team.getName())
                            .build())
                    .build()));
            withSubmissions(contest.getSubmissions());

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
            this.results = contest.results.entrySet().stream().collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue().clone()));
            this.contestType = contest.contestType;
        }

        public Builder(Map<String, Participant> teams, Map<String, TeamDAO> teamList, ContestType calculator) {
            this.contestType = calculator;
            teams.values().forEach(team -> results.put(team.getName(), new ParticipantResult.Builder().withCalculator(calculator).withParticipant(new Participant.Builder().withId(team.getId()).withName(team.getName()).build()).build()));
            withTeamInfo(teamList);
        }


        public Builder withSubmissions(List<SubmissionNode> submissions) {
            for (SubmissionNode submit : submissions) {
                if (submit.getUsername() != null) {
                    results.putIfAbsent(submit.getUsername(), new ParticipantResult.Builder().withCalculator(getCalculator()).withParticipant(new Participant.Builder().withId(submit.getUserId()).withName(submit.getUsername()).build()).build());
                    results.get(submit.getUsername()).pushSubmit(new SubmissionNode.Builder(submit).build());
                }
            }
            return this;
        }

        public Builder withTasks(List<Task> tasks) {
            this.tasks = tasks.stream().map(task -> new Task.Builder(task).build()).collect(Collectors.toList());
            return this;
        }

        public Builder withStopTime(LocalDateTime time) {
            this.stopTime = Optional.ofNullable(time).orElse(LocalDateTime.MAX);
            return this;
        }

        public Builder withStartTime(LocalDateTime time) {
            this.startTime = time;
            return this;
        }

        public Builder withCurrentTime(LocalDateTime time) {
            this.currentTime = time;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withFogTime(Long fogTime) {
            this.fogTime = fogTime;
            return this;
        }

        public Builder withUnFogTime(Long unFogTime) {
            this.unfogTime = unFogTime;
            return this;
        }

        public Builder withId(Long contestId) {
            this.contestId = contestId;
            return this;
        }


        private ContestType getCalculator() {
            if (contestType == null) {
                contestType = ContestType.ACM;
            }
            return contestType;
        }

        public Builder withDuration(Long duration) {
            this.duration = Optional.ofNullable(duration).orElse(1L);
            return this;
        }

        public Builder withTeamInfo(Map<String, TeamDAO> teamList) {
            BiFunction<ParticipantResult, TeamDAO, ParticipantResult> updateteamInfo = (result, info) -> new ParticipantResult
                    .Builder(result)
                    .withParticipant(new Participant.Builder()
                            .withId(result.getParticipant().getId())
                            .withName(info.getName())
                            .withUniversity(info.getUniversity())
                            .build())
                    .build();

            teamList.entrySet().forEach(team -> results.computeIfPresent(team.getKey(), (key, value) -> updateteamInfo.apply(results.get(key), team.getValue())));
            return this;
        }

        public Contest build() {
            return new Contest(this);
        }

    }
}
