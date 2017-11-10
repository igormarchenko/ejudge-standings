package org.ssu.standings.entity;

import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.repository.TeamRepository;
import org.ssu.standings.entity.contestresponse.Contest;
import org.ssu.standings.entity.contestresponse.ParticipantResult;
import org.ssu.standings.entity.contestresponse.ParticipantUpdates;
import org.ssu.standings.event.ContestUpdates;
import org.ssu.standings.event.ContestUpdatesEventProducer;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.parser.entity.SubmissionNode;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class ContestDataStorage {
    @Resource
    private TeamRepository teamRepository;

    @Resource
    private ContestUpdatesEventProducer contestUpdatesEventProducer;

    @Resource
    private ContestMerger contestMerger;

    private Map<Long, Contest> contestData = new HashMap<>();
    private Map<String, TeamDAO> teams;
    private Map<Long, Boolean> isContestFrozen = new HashMap<>();
    private BiPredicate<Contest, SubmissionNode> isSubmitFrozen = (contest, submit) -> contest.getStartTime().plusSeconds(submit.getTime()).compareTo(contest.getStopTime().minusSeconds(contest.getFogTime())) > 0 && isContestFrozen.get(submit.getProblemId() / 1_000_000);

    public void updateData() {
        teams = teamRepository.findAll()
                .stream()
                .collect(Collectors.toMap(TeamDAO::getName, Function.identity(), (existingTeam, newTeam) -> existingTeam));

    }

    public void setTeams(Map<String, TeamDAO> teams) {
        this.teams = teams;
    }

    private ContestSubmissionsChanges getDifferenceWithContest(Long contestId, Contest contest) {
        Map<String, SubmissionNode> submissions = getContestSubmissions(contestId).stream().collect(Collectors.toMap(submit -> submit.getRunUuid(), submit -> submit, (a, b) -> a));

        Function<Predicate<SubmissionNode>, List<SubmissionNode>> filterSubmissions = (predicate) -> contest.getSubmissions()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());

        List<SubmissionNode> newSubmissions = filterSubmissions.apply(submit -> !submissions.containsKey(submit.getRunUuid()));
        List<SubmissionNode> rejudgedSubmissions = filterSubmissions.apply(submit -> submissions.containsKey(submit.getRunUuid()) && submit.getStatus() != submissions.get(submit.getRunUuid()).getStatus());

        return new ContestSubmissionsChanges(newSubmissions, rejudgedSubmissions);
    }

    private void addContest(Long contestId, Contest contest) {
        contestData.put(contestId, new Contest.Builder(contest).build());
    }

    private List<SubmissionNode> getContestSubmissions(Long contestId) {
        return contestData.get(contestId).getSubmissions();
    }

    public List<ParticipantUpdates> getFrozenSubmits(Long contestId) {
        Contest contest = new Contest.Builder(getContestData(contestId)).build();
        List<SubmissionNode> frozenSubmits = getContestSubmissions(contestId).stream()
                .filter(submit -> isSubmitFrozen.test(contest, submit))
                .collect(Collectors.toList());

        List<ParticipantUpdates> result = new ArrayList<>();
        for(SubmissionNode submission : frozenSubmits) {
            ParticipantResult resultsBeforeUpdate = contest.getResults().stream().filter(userResult -> userResult.getParticipant().getName().equals(submission.getUsername())).findFirst().get();
            contest.updateSubmissions(Arrays.asList(submission));
            ParticipantResult resultsAfterUpdate = contest.getResults().stream().filter(userResult -> userResult.getParticipant().getName().equals(submission.getUsername())).findFirst().get();
            result.add(new ParticipantUpdates(submission.getUsername(), resultsAfterUpdate, resultsBeforeUpdate.getPlace() - 1, resultsAfterUpdate.getPlace() - 1));
        }
        return result;
    }

    public Contest getContestData(Long contestId) {
        if (!contestData.containsKey(contestId)) return null;
        Contest storedContest = contestData.get(contestId);
        Contest.Builder contest = new Contest.Builder(storedContest);

//        if (isContestFrozen.get(contestId)) {
            List<SubmissionNode> nodes = getContestSubmissions(contestId).stream()
                    .filter(submit -> isSubmitFrozen.test(storedContest, submit))
                    .map(submit -> new SubmissionNode.Builder(submit).withStatus(SubmissionStatus.FROZEN).build())
                    .collect(Collectors.toList());

            contest.withSubmissions(nodes);
//        }
        return contest.build();
    }

    private Boolean isContestPresent(Long contestId) {
        return contestData.containsKey(contestId);
    }

    public Contest updateContest(Long contestId, List<ContestNode> dataFromStandingsFile, Map<Long, Boolean> frozenStatuses) {
        isContestFrozen = new HashMap<>(frozenStatuses);
        Contest contest = contestMerger.mergeContests(dataFromStandingsFile, teams);

        if (!isContestPresent(contestId)) {
            addContest(contestId, contest);
        } else {
            ContestSubmissionsChanges contestSubmissionsChanges = getDifferenceWithContest(contestId, contest);

            List<ParticipantResult> resultsBeforeUpdate = contestData.get(contestId).getResults();
            addContest(contestId, contest);
            List<ParticipantResult> resultsAfterUpdate = contestData.get(contestId).getResults();

            Set<String> affectedTeamsIds = Stream.concat(contestSubmissionsChanges.getNewSubmissions().stream(), contestSubmissionsChanges.getChangedSubmissions().stream())
                            .map(SubmissionNode::getUsername)
                            .collect(Collectors.toSet());

            Function<List<ParticipantResult>, Map<String, Integer>> getTeamPlaces = (results) -> IntStream.range(0, results.size())
                    .boxed()
                    .filter(index -> affectedTeamsIds.contains(results.get(index).getParticipant().getName()))
                    .collect(Collectors.toMap(index -> results.get(index).getParticipant().getName(), index -> index));

            Map<String, Integer> placesBeforeUpdate = getTeamPlaces.apply(resultsBeforeUpdate);
            Map<String, Integer> placesAfterUpdate = getTeamPlaces.apply(resultsAfterUpdate);

            Map<String, ParticipantResult> affectedTeamsResults = contestData.get(contestId).getTeamsResults(affectedTeamsIds);

            Map<String, ParticipantUpdates> updatedResults = affectedTeamsIds.stream()
                    .map(teamId -> new ParticipantUpdates(teamId, affectedTeamsResults.get(teamId), placesBeforeUpdate.get(teamId), placesAfterUpdate.get(teamId)))
                    .collect(Collectors.toMap(ParticipantUpdates::getTeamId, team -> team));


            if (!affectedTeamsIds.isEmpty())
                contestUpdatesEventProducer.publishEvent(new ContestUpdates(contestId, updatedResults));
        }
        return getContestData(contestId);
    }
}
