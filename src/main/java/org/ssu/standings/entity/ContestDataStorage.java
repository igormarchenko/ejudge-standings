package org.ssu.standings.entity;

import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.repository.ContestRepository;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.ParticipantResult;
import org.ssu.standings.entity.contestresponse.ParticipantUpdates;
import org.ssu.standings.event.ContestUpdates;
import org.ssu.standings.event.ContestUpdatesEventProducer;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ContestDataStorage {
    @Resource
    private ContestUpdatesEventProducer contestUpdatesEventProducer;

    @Resource
    private ContestRepository contestRepository;

    private Map<Long, Contest> contestData = new HashMap<>();
    private Map<String, TeamDAO> teams;
    private Map<Long, Boolean> isContestFrozen = new HashMap<>();

    public void setTeams(Map<String, TeamDAO> teams) {
        this.teams = teams;
    }

    private ContestSubmissionsChanges getContestChanges(Long contestId, ContestNode response) {
        Map<String, SubmissionNode> submissions = getContestSubmissions(contestId);

        ContestSubmissionsChanges contestSubmissionsChanges = new ContestSubmissionsChanges();

        for (SubmissionNode submit : response.getSubmissions()) {
            SubmissionNode excitingSubmit = submissions.get(submit.getRunUuid());
            if (excitingSubmit == null) {
                contestSubmissionsChanges.addNewSubmission(submit);
            } else if (!excitingSubmit.equals(submit)) {
                contestSubmissionsChanges.addRejudgedSubmission(submit);
            }
        }
        return contestSubmissionsChanges;
    }

    private void addContest(Long contestId, ContestNode contest, Boolean isFrozen) {
        isContestFrozen.put(contestId, isFrozen);
        contestData.put(contestId, new Contest.Builder(contest, teams).build());
    }

    private Map<String, SubmissionNode> getContestSubmissions(Long contestId) {
        return contestData.get(contestId).getResults()
                .stream()
                .flatMap(result -> result.getResults().values().stream().flatMap(res -> res.getSubmissions().stream()))
                .collect(Collectors.toMap(SubmissionNode::getRunUuid, Function.identity()));
    }

    public Contest getContestData(Long contestId) {
        ArrayList<SubmissionNode> submissions = new ArrayList<>(getContestSubmissions(contestId).values());
        Contest contest = contestData.get(contestId);
        if(isContestFrozen.get(contestId)) {

            submissions.stream()
                    .filter(submit -> contest.getStartTime().plusSeconds(submit.getTime()).compareTo(contest.getStopTime().minusSeconds(contest.getFogTime())) > 0)
                    .forEach(submit -> submit.setStatus(SubmissionStatus.FROZEN));
        }
        return new Contest.Builder(contest).withSubmissions(submissions).build();
    }

    private Boolean isContestPresent(Long contestId) {
        return contestData.containsKey(contestId);
    }

    public Contest updateContest(Long contestId, ContestNode dataFromStandingsFile, Boolean isFrozen) {
        isContestFrozen.put(contestId, isFrozen);
        if (!isContestPresent(contestId)) {
            addContest(contestId, dataFromStandingsFile, isFrozen);
        } else {
            ContestSubmissionsChanges contestSubmissionsChanges = getContestChanges(contestId, dataFromStandingsFile);

            List<ParticipantResult> resultsBeforeUpdate = getContestData(contestId).getResults();
            //TODO: add rejudged submissions
            getContestData(contestId).updateSubmissions(contestSubmissionsChanges.getNewSubmissions());
            List<ParticipantResult> resultsAfterUpdate = getContestData(contestId).getResults();

            Set<Long> teamsIds = contestSubmissionsChanges.getNewSubmissions().stream().map(SubmissionNode::getUserId).collect(Collectors.toSet());

            Map<Long, Integer> placesBeforeUpdate = IntStream.range(0, resultsBeforeUpdate.size())
                    .filter(index -> teamsIds.contains(resultsBeforeUpdate.get(index).getParticipant().getId()))
                    .boxed()
                    .collect(Collectors.toMap(index -> resultsBeforeUpdate.get(index).getParticipant().getId(), index -> index));

            Map<Long, Integer> placesAfterUpdate = IntStream.range(0, resultsAfterUpdate.size())
                    .filter(index -> teamsIds.contains(resultsAfterUpdate.get(index).getParticipant().getId()))
                    .boxed()
                    .collect(Collectors.toMap(index -> resultsAfterUpdate.get(index).getParticipant().getId(), index -> index));

            Map<Long, ParticipantResult> affectedTeams = getContestData(contestId).getTeamsResults(teamsIds);

            Map<Long, ParticipantUpdates> collect = teamsIds.stream()
                    .map(teamId -> new ParticipantUpdates(teamId, affectedTeams.get(teamId), placesBeforeUpdate.get(teamId), placesAfterUpdate.get(teamId)))
                    .collect(Collectors.toMap(ParticipantUpdates::getTeamId, team -> team));

            if (!teamsIds.isEmpty())
                contestUpdatesEventProducer.publishEvent(new ContestUpdates(contestId, collect));
        }
        return getContestData(contestId);
    }
}
