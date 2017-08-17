package org.ssu.standings.service;

import org.springframework.stereotype.Service;

@Service
public class BaylorExportService {
//    @Resource
//    private StandingsWatchService standingsWatchService;

//    public List<BaylorTeam> getResultsForBaylor() throws ParserConfigurationException {
//        Function<String, Optional<ContestDEPRECATED>> getContestForTeam = (teamName) -> standingsWatchService.getContestData()
//                .stream()
//                .filter(contest -> contest.getTeamId(teamName).isPresent())
//                .findAny();
//
//        BaylorStandingsParser parser = new BaylorStandingsParser("C:\\baylor.xml");
//        NodeListWrapper teams = new NodeListWrapper(parser.getTeamDAOS());
//
//        List<BaylorTeam> baylorTeams = new ArrayList<>();
//        for (Node node : teams) {
//            BaylorTeam baylorTeam = new BaylorTeam().setTeamName(parser.getAttributeValue(node, "TeamName"))
//                    .setTeamId(Long.parseLong(parser.getAttributeValue(node, "TeamID")));
//
//            Optional<ContestDEPRECATED> contest = getContestForTeam.apply(baylorTeam.getTeamName());
//
//            if (contest.isPresent()) {
//                List<SubmissionNode> submissions = contest.get().getTeamSubmissions(contest.get().getTeamId(baylorTeam.getTeamName()).get());
//                List<SubmissionNode> acceptedSubmissions = getAcceptedSubmissions(submissions);
//                Long acceptedTasks = acceptedSubmissions.stream().map(SubmissionNode::getProblemId).distinct().count();
//                baylorTeam.setLastProblemTime(calculateLastProblemTime(acceptedSubmissions))
//                        .setProblemsSolved(Math.toIntExact(acceptedTasks))
//                        .setTotalTime(Math.toIntExact(calculatePenalty(submissions)));
//            }
//            baylorTeams.add(baylorTeam);
//        }
//
//        Collections.sort(baylorTeams);
//
//
//        for (int i = 0; i < baylorTeams.size(); i++) {
//            baylorTeams.get(i).setRank(i + 1);
//        }
//
//        return baylorTeams;
//        return null;
//    }

    //    private Long calculatePenalty(List<Submission> submissions) {
//        Map<Long, List<Submission>> acceptedSubmits = submissions.stream()
//                .filter(item -> item.getStatus().equals("OK"))
//                .collect(Collectors.groupingBy(Submission::getProblemId));
//
//        Map<Long, List<Submission>> submitsBeforeAccept = submissions.stream()
//                .filter(item -> !"CE".equals(item.getStatus()))
//                .filter(item -> acceptedSubmits.containsKey(item.getProblemId()))
//                .filter(item -> acceptedSubmits.get(item.getProblemId()).get(0).getTime() >= item.getTime())
//                .collect(Collectors.groupingBy(Submission::getProblemId));
//
//        return acceptedSubmits.entrySet().stream()
//                .mapToLong(item -> roundTime(item.getValue().get(0).getTime()) + (submitsBeforeAccept.get(item.getKey()).size() - 1) * 20)
//                .sum();
//    }
//
//    private Long calculateLastProblemTime(List<Submission> acceptedSubmissions) {
//        Optional<Submission> lastSubmit = acceptedSubmissions.stream().reduce((a, b) -> b);
//        return lastSubmit.isPresent() ? roundTime(lastSubmit.get().getTime()) : 0;
//    }
//
//    private List<Submission> getAcceptedSubmissions(List<Submission> submissions) {
//        return submissions
//                .stream()
//                .filter(submit -> "OK".equals(submit.getStatus()))
//                .collect(Collectors.toList());
//    }
//
    private Long roundTime(Long time) {
        return (long) Math.ceil(time / 60.0);
    }
}

