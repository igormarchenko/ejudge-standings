package org.ssu.standings.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ssu.standings.dao.entity.StandingsFile;
import org.ssu.standings.dao.repository.StandingsFilesRepository;
import org.ssu.standings.dao.repository.TeamRepository;
import org.ssu.standings.entity.ContestStandingsFileObserver;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class StandingsWatchService {
    @Resource
    private TeamRepository teamRepository;

    @Resource
    private StandingsFilesRepository standingsFilesRepository;

//    private Map<Long, FileWatcher> watchers;

//    @PostConstruct
//    public void initOld() throws ParserConfigurationException, IOException, SAXException {
//        updateWatchers();
//        updateChanges();
//    }

//    public void updateWatchers() {
//        TeamInUniversityList.setTeamUniversity(teamRepository.findAll().stream().collect(Collectors.toMap(item -> item.getName().trim(), Team::getUniversityEntity, (a, b) -> a)));
//
//        Map<Long, List<StandingsFile>> contestDescriptions = standingsFilesRepository.findAll()
//                .stream()
//                .collect(Collectors.groupingBy(StandingsFile::getContestId));
//
//        Map<Long, ContestInfo> contests = contestDescriptions.entrySet()
//                .stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, item -> new ContestInfo.Builder(item.getValue()).build()));
//
//        watchers = contests.entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, item -> new FileWatcher(item.getValue().getUrlsFromDescriptions())
//                        .setContestId(item.getValue().getContestId())
//                        .setIsFinalResults(item.getValue().getIsFinal())
//                ));
//
//    }

//    public Map<Long, String> getContestList() {
//        return watchers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, item -> item.getValue().getContestDEPRECATED().getName()));
//    }

//    @Scheduled(fixedDelay = 1000)
//    public void updateChanges() {
//        watchers.values().forEach(FileWatcher::updateChanges);
//    }

//    public List<SubmissionNode> getLastSubmissions(Long contestId, Long time) {
//        return getContestData(contestId).getSubmissions()
//                .stream()
//                .filter(item -> item.getTime() > time)
//                .collect(Collectors.toList());
//    }
//
//    public ContestDEPRECATED getContestData(Long contestId) {
//        return watchers.get(contestId).getContestData();
//    }
//
//    public Map<Long, Map<Long, List<SubmissionNode>>> getFrozenResults(Long contestId) {
//        return watchers.get(contestId).getFrozenSubmissions().stream().collect(Collectors.groupingBy(SubmissionNode::getUserId, Collectors.groupingBy(SubmissionNode::getProblemId)));
//    }
//


    private Map<StandingsFile, ContestStandingsFileObserver> observers;

    @PostConstruct
    public void init() {
        observers = standingsFilesRepository.findAll().stream().collect(Collectors.toMap(Function.identity(), ContestStandingsFileObserver::new));
    }

    @Scheduled(fixedDelay = 1000)
    public void update() {
        observers.values().forEach(observer -> {
            try {
                observer.update();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getContestData(Long contestId) {
        List<String> contestContent = observers
                .entrySet()
                .stream()
                .filter(observer -> observer.getKey().getContestId().equals(contestId))
                .map(observer -> observer.getValue().getContent())
                .collect(Collectors.toList());
    }
}
