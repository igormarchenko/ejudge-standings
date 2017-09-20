package org.ssu.standings.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.ssu.standings.entity.BaylorResults;
import org.ssu.standings.entity.ContestDataStorage;
import org.ssu.standings.entity.SubmissionStatus;
import org.ssu.standings.entity.TeamResultsInBaylor;
import org.ssu.standings.entity.contestresponse.ParticipantResult;
import org.ssu.standings.entity.contestresponse.TaskResult;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BaylorExportService {
    @Resource
    private ContestDataStorage storage;

    private Map<String, TeamResultsInBaylor> exportBaylorTeamFromFile(String content) {
        List<TeamResultsInBaylor> teams = new ArrayList<>();
        try {
            teams = new XmlMapper().readValue(content, new TypeReference<List<TeamResultsInBaylor>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teams.stream().collect(Collectors.toMap(TeamResultsInBaylor::getTeamName, Function.identity()));

    }

    public BaylorResults getContestResults(Long contestId, String baylorFileContent) {
        Map<String, TeamResultsInBaylor> baylorTeamList = exportBaylorTeamFromFile(baylorFileContent);
        Function<Map<Long, TaskResult>, Long> getLastSolvedProblemTime = (results) -> results.values()
                .stream()
                .filter(task -> task.getStatus() == SubmissionStatus.OK)
                .map(TaskResult::getFirstAcceptedTime)
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .orElse(0L);

        List<ParticipantResult> results = storage.getContestData(contestId).getResults()
                .stream()
                .filter(team -> baylorTeamList.containsKey(team.getParticipant().getName()))
                .collect(Collectors.toList());

        List<TeamResultsInBaylor> teamResults = IntStream.range(0, results.size())
                .mapToObj(index -> new TeamResultsInBaylor.Builder(baylorTeamList.getOrDefault(results.get(index).getParticipant().getName(), new TeamResultsInBaylor()))
                        .withLastProblemTime(getLastSolvedProblemTime.apply(results.get(index).getResults()))
                        .withProblemsSolved(results.get(index).solvedProblems())
                        .withTotalTime(results.get(index).getPenalty())
                        .withRank(index + 1)
                        .build())
                .collect(Collectors.toList());

        return new BaylorResults(teamResults);
    }
}

