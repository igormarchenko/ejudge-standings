package org.ssu.standings.entity;

import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.Participant;
import org.ssu.standings.entity.contestresponse.Task;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.ParticipantNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ContestMerger {
    public Contest mergeContests(List<ContestNode> contests, Map<String, TeamDAO> teams) {
        List<Contest> contestList = contests.stream().map(contest -> new Contest.Builder(contest)
                .withSubmissions(contest.getSubmissions().stream().map(submit -> new SubmissionNode.Builder(submit).withUsername(contest.getTeamNameById(submit.getUserId())).build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());

        Contest currentContest = contestList.stream().sorted(Comparator.comparing(Contest::getStartTime)).reduce((a, b) -> b).get();

        return new Contest.Builder()
                .withId(currentContest.getContestId())
                .withDuration(currentContest.getDuration())
                .withName(currentContest.getName())
                .withTasks(createTaskList(contests))
                .withTeams(createParticipantList(contests, teams))
                .withTeamInfo(teams)
                .withStartTime(currentContest.getStartTime())
                .withStopTime(currentContest.getStopTime())
                .withCurrentTime(currentContest.getCurrentTime())
                .withFogTime(currentContest.getFogTime())
                .withUnFogTime(currentContest.getUnfogTime())
                .withSubmissions(getSubmissions(contestList))

                .build();
    }

    private Map<String, Participant> createParticipantList(List<ContestNode> contests, Map<String, TeamDAO> teams) {
        Function<String, UniversityDAO> findUniversityForTeam = (team) -> teams.getOrDefault(team, new TeamDAO()).getUniversity();

        return contests
                .stream()
                .flatMap(contest -> contest.getParticipants().stream())
                .collect(Collectors.toMap(ParticipantNode::getName, team -> new Participant.Builder().withId(team.getId()).withName(team.getName()).withUniversity(findUniversityForTeam.apply(team.getName())).build(), (a, b) -> a));
    }

    private List<Task> createTaskList(List<ContestNode> contests) {
        List<Contest> contestList = contests.stream().map(contest -> new Contest.Builder(contest).build()).collect(Collectors.toList());
        BiFunction<Task, Integer, String> transformTaskTitle = (task, index) -> (contests.size() > 1) ? task.getShortName() + Integer.toString(index + 1) : task.getShortName();
        return IntStream.range(0, contestList.size())
                .boxed()
                .flatMap(index -> contestList.get(index).getTasks().stream()
                        .map(task -> new Task.Builder(task).withId(contestList.get(index).getContestId() * 100 + task.getId()).withShortName(transformTaskTitle.apply(task, index)).build())
                )
                .collect(Collectors.toList());
    }

    private List<SubmissionNode> getSubmissions(List<Contest> contestList) {
        Function<Contest, List<SubmissionNode>> getParticipantResultsFromContest = (contest) -> contest
                .getResults()
                .stream()
                .flatMap(result -> result.getResults().values().stream())
                .flatMap(taskResult -> taskResult.getSubmissions().stream())
                .peek(submit -> submit.setProblemId(contest.getContestId() * 100 + submit.getProblemId()))
                .collect(Collectors.toList());

        return contestList.stream().flatMap(contest -> getParticipantResultsFromContest.apply(contest).stream()).collect(Collectors.toList());
    }
}
