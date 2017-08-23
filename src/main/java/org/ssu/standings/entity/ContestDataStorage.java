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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
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
    private BiPredicate<Contest, SubmissionNode> isSubmitFrozen = (contest, submit) -> contest.getStartTime().plusSeconds(submit.getTime()).compareTo(contest.getStopTime().minusSeconds(contest.getFogTime())) > 0;

    public void setTeams(Map<String, TeamDAO> teams) {
        this.teams = teams;
    }

    private ContestSubmissionsChanges getDifferenceWithContest(Long contestId, ContestNode contest) {
        Map<String, SubmissionNode> submissions = getContestSubmissions(contestId);
        Function<Predicate<SubmissionNode>, List<SubmissionNode>> filterSubmissions = (predicate) -> contest.getSubmissions()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());

        List<SubmissionNode> newSubmissions = filterSubmissions.apply(submit -> !submissions.containsKey(submit.getRunUuid()));
        List<SubmissionNode> rejudgedSubmissions = filterSubmissions.apply(submit -> submissions.containsKey(submit.getRunUuid()) &&
                !submit.equals(submissions.get(submit.getRunUuid())));

        return new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
    }

    private void addContest(Long contestId, ContestNode contest) {
        contestData.put(contestId, new Contest.Builder(contest, teams).build());
    }

    private Map<String, SubmissionNode> getContestSubmissions(Long contestId) {
        return contestData.get(contestId).getResults()
                .stream()
                .flatMap(result -> result.getResults().values().stream().flatMap(res -> res.getSubmissions().stream()))
                .collect(Collectors.toMap(SubmissionNode::getRunUuid, Function.identity()));
    }

    public List<SubmissionNode> getFrozenSubmits(Long contestId) {
        Contest contest = contestData.get(contestId);
        return getContestSubmissions(contestId).values().stream()
                .filter(submit -> isSubmitFrozen.test(contest, submit))
                .collect(Collectors.toList());
    }

    public Contest getContestData(Long contestId) {
        Contest contest = new Contest.Builder(contestData.get(contestId)).build();
        if (isContestFrozen.get(contestId)) {
            getContestSubmissions(contestId).values().stream()
                    .filter(submit -> isSubmitFrozen.test(contest, submit))
//                    .map(SubmissionNode::clone)
                    .forEach(submit -> submit.setStatus(SubmissionStatus.FROZEN));
        }
        return contest;
    }

    private Boolean isContestPresent(Long contestId) {
        return contestData.containsKey(contestId);
    }

    public Contest updateContest(Long contestId, ContestNode dataFromStandingsFile, Boolean isFrozen) {
        isContestFrozen.put(contestId, isFrozen);
        if (!isContestPresent(contestId)) {
            addContest(contestId, dataFromStandingsFile);
        } else {
            ContestSubmissionsChanges contestSubmissionsChanges = getDifferenceWithContest(contestId, dataFromStandingsFile);

            List<ParticipantResult> resultsBeforeUpdate = contestData.get(contestId).getResults();
            //TODO: add rejudged submissions
            getContestData(contestId).updateSubmissions(contestSubmissionsChanges.getNewSubmissions());
            List<ParticipantResult> resultsAfterUpdate = contestData.get(contestId).getResults();

            Set<Long> affectedTeamsIds = contestSubmissionsChanges.getNewSubmissions().stream().map(SubmissionNode::getUserId).collect(Collectors.toSet());

            Function<List<ParticipantResult>, Map<Long, Integer>> getTeamPlaces = (results) -> IntStream.range(0, results.size())
                    .boxed()
                    .filter(index -> affectedTeamsIds.contains(results.get(index).getParticipant().getId()))
                    .collect(Collectors.toMap(index -> results.get(index).getParticipant().getId(), index -> index));

            Map<Long, Integer> placesBeforeUpdate = getTeamPlaces.apply(resultsBeforeUpdate);
            Map<Long, Integer> placesAfterUpdate = getTeamPlaces.apply(resultsAfterUpdate);

            Map<Long, ParticipantResult> affectedTeamsResults = getContestData(contestId).getTeamsResults(affectedTeamsIds);

            Map<Long, ParticipantUpdates> updatedResults = affectedTeamsIds.stream()
                    .map(teamId -> new ParticipantUpdates(teamId, affectedTeamsResults.get(teamId), placesBeforeUpdate.get(teamId), placesAfterUpdate.get(teamId)))
                    .collect(Collectors.toMap(ParticipantUpdates::getTeamId, team -> team));

            if (!affectedTeamsIds.isEmpty())
                contestUpdatesEventProducer.publishEvent(new ContestUpdates(contestId, updatedResults));
        }
        return getContestData(contestId);
    }
}
