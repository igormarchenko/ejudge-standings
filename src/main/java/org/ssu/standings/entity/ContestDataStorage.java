package org.ssu.standings.entity;

import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.ParticipantResult;
import org.ssu.standings.entity.contestresponse.ParticipantUpdates;
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
import java.util.stream.IntStream;

@Component
public class ContestDataStorage {
    @Resource
    private ContestUpdatesEventProducer contestUpdatesEventProducer;

    private Map<Long, Contest> contestData = new HashMap<>();
    private Map<String, List<TeamDAO>> teams;

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


    private void addContest(Long contestId, ContestNode contest) {
        contestData.put(contestId, new Contest.Builder(contest, teams).build());
    }

    public Contest getContestData(Long contestId) {
        return contestData.get(contestId);
    }

    private Boolean contains(Long contestId) {
        return contestData.containsKey(contestId);
    }

    public Contest updateContest(Long contestId, ContestNode contestNode) {
        if (!contains(contestId)) {
            addContest(contestId, contestNode);
        } else {
            ContestSubmissionsChanges contestSubmissionsChanges = getContestChanges(contestId, contestNode);

            List<ParticipantResult> resultsBeforeUpdate = getContestData(contestId).getResults();
            getContestData(contestId).updateSubmissions(contestSubmissionsChanges.getNewSubmissions());
            List<ParticipantResult> resultsAfterUpdate = getContestData(contestId).getResults();

            Set<Long> teamsIds = contestSubmissionsChanges.getNewSubmissions().stream().map(SubmissionNode::getUserId).collect(Collectors.toSet());

            Map<Long, Integer> placesBeforeUpdate = IntStream.range(0, resultsBeforeUpdate.size()).filter(index -> teamsIds.contains(resultsBeforeUpdate.get(index).getParticipant().getId())).boxed().collect(Collectors.toMap(index -> resultsBeforeUpdate.get(index).getParticipant().getId(), index -> index));
            Map<Long, Integer> placesAfterUpdate = IntStream.range(0, resultsAfterUpdate.size()).filter(index -> teamsIds.contains(resultsAfterUpdate.get(index).getParticipant().getId())).boxed().collect(Collectors.toMap(index -> resultsAfterUpdate.get(index).getParticipant().getId(), index -> index));

            Map<Long, ParticipantResult> affectedTeams = getContestData(contestId).getTeamsResults(teamsIds);

            Map<Long, ParticipantUpdates> collect = teamsIds.stream().map(teamId -> new ParticipantUpdates(teamId, affectedTeams.get(teamId), placesBeforeUpdate.get(teamId), placesAfterUpdate.get(teamId))).collect(Collectors.toMap(ParticipantUpdates::getTeamId, team -> team));

            if (!teamsIds.isEmpty())
                contestUpdatesEventProducer.publishEvent(new ContestUpdates(contestId, collect));
        }
        return getContestData(contestId);
    }
}
