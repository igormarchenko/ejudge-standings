package org.ssu.standings.utils;

public class ContestsMerger {
//    public static ContestDEPRECATED merge(List<ContestDEPRECATED> contestDEPRECATEDS) {
//        ContestDEPRECATED anyContestDEPRECATED = contestDEPRECATEDS.stream().findFirst().orElseThrow(NullPointerException::new);
//
//        List<TeamDAO> teamDAOS = contestDEPRECATEDS.stream().flatMap(item -> item.getTeamDAOS().stream()).collect(Collectors.toSet())
//                .stream()
//                .collect(Collectors.toList());
//
//        List<Submission> submissions = contestDEPRECATEDS.stream().flatMap(item -> item.getSubmissions().stream())
//                .sorted(Comparator.comparing(Submission::getTime))
//                .collect(Collectors.toList());
//
//        return new ContestDEPRECATED().setContestId(anyContestDEPRECATED.getContestId())
//                .setName(anyContestDEPRECATED.getName())
//                .setTeamDAOS(teamDAOS)
//                .setSubmissions(submissions)
//                .setTasks(anyContestDEPRECATED.getTasks())
//                .setCurrentTime(anyContestDEPRECATED.getCurrentTime())
//                .setBeginTime(anyContestDEPRECATED.getBeginTime())
//                .setEndTime(anyContestDEPRECATED.getEndTime())
//                .setFrozenTime(anyContestDEPRECATED.getFrozenTime())
//                .setUnfrozenTime(anyContestDEPRECATED.getUnfrozenTime())
//                .setIsFinalResults(anyContestDEPRECATED.getIsFinalResults());
//
//    }
}
