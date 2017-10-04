package org.ssu.standings.entity;

import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.Task;
import org.ssu.standings.parser.entity.ContestNode;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ContestMerger {
    public Contest mergeContests(List<ContestNode> contests, Map<String, TeamDAO> teams) {
        List<Contest> contestList = contests.stream().map(contest -> new Contest.Builder(contest, teams).build()).collect(Collectors.toList());
        List<Task> tasks = IntStream.range(0, contestList.size())
                .boxed()
                .flatMap(index -> contestList.get(index).getTasks().stream()
                        .map(task -> new Task.Builder(task).withId(contestList.get(index).getContestId() * 100 + task.getId()).withShortName(task.getShortName() + Integer.toString(index + 1)).build()))
                .collect(Collectors.toList());

        Contest currentContest = contestList.stream().sorted(Comparator.comparing(Contest::getStartTime)).reduce((a, b) -> b).get();

        LocalDateTime startTime = currentContest.getStartTime();
        LocalDateTime stopTime = currentContest.getStopTime();
        LocalDateTime currentTime = currentContest.getCurrentTime();

        String name = currentContest.getName();
        Long fogTime = currentContest.getFogTime();
        Long unFogTime = currentContest.getUnfogTime();

        return new Contest.Builder(contests.get(0), teams)
                .withName(name)
                .withTasks(tasks)
                .withStartTime(startTime)
                .withStopTime(stopTime)
                .withCurrentTime(currentTime)
                .withFogTime(fogTime)
                .withUnFogTime(unFogTime)
                .build();
    }
}
