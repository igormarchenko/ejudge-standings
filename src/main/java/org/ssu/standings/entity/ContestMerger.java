package org.ssu.standings.entity;

import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.Task;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ContestMerger {
    public Contest mergeContests(List<ContestNode> contests, Map<String, TeamDAO> teams) {
        List<Contest> contestList = contests.stream().map(contest -> new Contest.Builder(contest, teams).build()).collect(Collectors.toList());

        List<Task> tasks = (contestList.size() > 1) ? IntStream.range(0, contestList.size())
                .boxed()
                .flatMap(index -> contestList.get(index).getTasks().stream()
                        .map(task -> new Task.Builder(task).withId(contestList.get(index).getContestId() * 100 + task.getId()).withShortName(task.getShortName() + Integer.toString(index + 1)).build()))
                .collect(Collectors.toList()) : new ArrayList<>(contestList.get(0).getTasks());

        Contest currentContest = contestList.stream().sorted(Comparator.comparing(Contest::getStartTime)).reduce((a, b) -> b).get();

        LocalDateTime startTime = currentContest.getStartTime();
        LocalDateTime stopTime = currentContest.getStopTime();
        LocalDateTime currentTime = currentContest.getCurrentTime();

        String name = currentContest.getName();
        Long fogTime = currentContest.getFogTime();
        Long unFogTime = currentContest.getUnfogTime();

        Function<Contest, List<SubmissionNode>> getParticipantResultsFromContest = (contest) -> contest
                .getResults()
                .stream()
                .flatMap(result -> result.getResults().values().stream())
                .flatMap(taskResult -> taskResult.getSubmissions().stream())
                .peek(submit -> submit.setProblemId(contest.getContestId() * 100 + submit.getProblemId()))
                .collect(Collectors.toList());

        List<SubmissionNode> submissions = contestList.stream().flatMap(contest -> getParticipantResultsFromContest.apply(contest).stream()).collect(Collectors.toList());

        return new Contest.Builder(contests.get(0), teams)
                .withName(name)
                .withTasks(tasks)
                .withStartTime(startTime)
                .withStopTime(stopTime)
                .withCurrentTime(currentTime)
                .withFogTime(fogTime)
                .withUnFogTime(unFogTime)
                .withSubmissions(submissions)
                .build();
    }
}
