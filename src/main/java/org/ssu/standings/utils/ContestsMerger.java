package org.ssu.standings.utils;

import org.ssu.standings.entity.ContestDEPRECATED;
import org.ssu.standings.entity.Submission;
import org.ssu.standings.dao.entity.Team;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ContestsMerger {
    public static ContestDEPRECATED merge(List<ContestDEPRECATED> contestDEPRECATEDS) {
        ContestDEPRECATED anyContestDEPRECATED = contestDEPRECATEDS.stream().findFirst().orElseThrow(NullPointerException::new);

        List<Team> teams = contestDEPRECATEDS.stream().flatMap(item -> item.getTeams().stream()).collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toList());

        List<Submission> submissions = contestDEPRECATEDS.stream().flatMap(item -> item.getSubmissions().stream())
                .sorted(Comparator.comparing(Submission::getTime))
                .collect(Collectors.toList());

        return new ContestDEPRECATED().setContestId(anyContestDEPRECATED.getContestId())
                .setName(anyContestDEPRECATED.getName())
                .setTeams(teams)
                .setSubmissions(submissions)
                .setTasks(anyContestDEPRECATED.getTasks())
                .setCurrentTime(anyContestDEPRECATED.getCurrentTime())
                .setBeginTime(anyContestDEPRECATED.getBeginTime())
                .setEndTime(anyContestDEPRECATED.getEndTime())
                .setFrozenTime(anyContestDEPRECATED.getFrozenTime())
                .setUnfrozenTime(anyContestDEPRECATED.getUnfrozenTime())
                .setIsFinalResults(anyContestDEPRECATED.getIsFinalResults());

    }
}
