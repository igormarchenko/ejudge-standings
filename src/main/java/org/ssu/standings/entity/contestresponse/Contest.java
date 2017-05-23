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

    @JsonIgnore
    private Map<String, SubmissionNode> submissions = new HashMap<>();

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
        submissions = builder.submissions;
    }

    @JsonProperty("results")
    public List<ParticipantResult> getResults() {
        //TODO: add lazy update
        return results.values().stream().sorted().collect(Collectors.toList());
    }

    public Map<String, SubmissionNode> getSubmissions() {
        return submissions;
    }

    public Map<Long, ParticipantResult> getTeamsResults(Collection<Long> teams) {
        return teams.stream().map(teamId -> results.get(teamId)).collect(Collectors.toMap(team -> team.getParticipant().getId(), team -> team));
    }

    public Contest updateSubmissions(List<SubmissionNode> newSubmissions) {
        newSubmissions.forEach(submit -> {
            results.get(submit.getUserId()).pushSubmit(submit);
            submissions.put(submit.getRunUuid(), submit);
        });
        return this;
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
        private Map<String, SubmissionNode> submissions = new HashMap<>();

        public Builder(ContestNode contest, Map<String, List<TeamDAO>> teams) {
            Function<String, UniversityDAO> getUniversityForTeam = teamName -> Optional.ofNullable(teams.get(teamName)).map(teamDAOS -> teamDAOS.get(0).getUniversity()).orElse(null);
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
                    .map(team -> new Participant(team, getUniversityForTeam.apply(team.getName())))
                    .collect(Collectors.toMap(Participant::getId, ParticipantResult::new));

            contest.getSubmissions().forEach(submit -> {
                results.get(submit.getUserId()).pushSubmit(submit);
                submissions.put(submit.getRunUuid(), submit);
            });
        }

        public Contest build() {
            return new Contest(this);
        }
    }
}
