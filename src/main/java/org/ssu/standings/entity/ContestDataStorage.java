package org.ssu.standings.entity;

import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.ParticipantResult;
import org.ssu.standings.event.ContestUpdates;
import org.ssu.standings.event.ContestUpdatesEventProducer;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ContestDataStorage {
    @Resource
    private ContestUpdatesEventProducer contestUpdatesEventProducer;

    private Map<Long, Contest> contestData = new HashMap<>();
    private Map<String, List<TeamDAO>> teams;
    private Map<Long, Map<Long, ParticipantResult>> changes = new HashMap<>();

    public void setTeams(Map<String, List<TeamDAO>> teams) {
        this.teams = teams;
    }

    private ContestSubmissionsChanges getContestChanges(Long contestId, ContestNode response) {
        ContestSubmissionsChanges contestSubmissionsChanges = new ContestSubmissionsChanges();
        for (SubmissionNode submit : response.getSubmissions()) {
            SubmissionNode excitingSubmit = contestData.get(contestId).getSubmissions().get(submit.getRunUuid());
            if (excitingSubmit == null) {
                contestSubmissionsChanges.addNewSubmission(submit);
            } else if (!submit.equals(excitingSubmit)) {
                contestSubmissionsChanges.addRejudgedSubmission(submit);
            }
        }
        return contestSubmissionsChanges;
    }


    public void addContest(Long contestId, ContestNode contest) {
        contestData.put(contestId, new Contest.Builder(contest, teams).build());
    }

    public Contest getContestData(Long contestId) {
        return contestData.get(contestId);
    }

    public Boolean contains(Long contestId) {
        return contestData.containsKey(contestId);
    }

    public Contest updateContest(Long contestId, ContestNode contestNode) {
        if (!contains(contestId)) {
            addContest(contestId, contestNode);
        } else {
            ContestSubmissionsChanges contestSubmissionsChanges = getContestChanges(contestId, contestNode);
            getContestData(contestId).updateSubmissions(contestSubmissionsChanges.getNewSubmissions());

            Set<Long> teamsIds = contestSubmissionsChanges.getNewSubmissions().stream().map(SubmissionNode::getUserId).collect(Collectors.toSet());
            Map<Long, ParticipantResult> affectedTeams = getContestData(contestId).getTeamsResults(teamsIds);
            changes.put(contestId, affectedTeams);
            if (!affectedTeams.isEmpty())
                contestUpdatesEventProducer.publishEvent(new ContestUpdates(contestId, affectedTeams));
        }
        return getContestData(contestId);
    }
}
