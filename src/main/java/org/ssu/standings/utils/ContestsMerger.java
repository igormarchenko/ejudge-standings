package org.ssu.standings.utils;

import org.ssu.standings.entity.Contest;
import org.ssu.standings.entity.Submission;
import org.ssu.standings.entity.Team;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ContestsMerger {
    public static Contest merge(List<Contest> contests) {
        Contest anyContest = contests.stream().findFirst().orElseThrow(NullPointerException::new);

        List<Team> teams = contests.stream().flatMap(item -> item.getTeams().stream()).collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toList());

        List<Submission> submissions = contests.stream().flatMap(item -> item.getSubmissions().stream())
                .sorted(Comparator.comparing(Submission::getTime))
                .collect(Collectors.toList());

        return new Contest().setContestId(anyContest.getContestId())
                .setName(anyContest.getName())
                .setTeams(teams)
                .setSubmissions(submissions)
                .setTasks(anyContest.getTasks())
                .setCurrentTime(anyContest.getCurrentTime())
                .setBeginTime(anyContest.getBeginTime())
                .setEndTime(anyContest.getEndTime())
                .setFrozenTime(anyContest.getFrozenTime())
                .setUnfrozenTime(anyContest.getUnfrozenTime())
                .setIsFinalResults(anyContest.getIsFinalResults());

    }
}
