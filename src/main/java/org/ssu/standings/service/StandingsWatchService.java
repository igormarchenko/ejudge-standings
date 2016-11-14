package org.ssu.standings.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ssu.standings.entity.*;
import org.ssu.standings.repository.ExternalFilesRepository;
import org.ssu.standings.repository.TeamRepository;
import org.ssu.standings.utils.FileWatcher;
import org.ssu.standings.utils.TeamInUniversityList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
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
    private ExternalFilesRepository externalFilesRepository;

    private Map<Long, FileWatcher> watchers;

    @PostConstruct
    public void init() throws ParserConfigurationException, IOException, SAXException {
       updateWatchers();
    }

    public void updateWatchers() {
        TeamInUniversityList.setTeamUniversity(teamRepository.findAll().stream().collect(Collectors.toMap(item -> item.getName().trim(), Team::getUniversityEntity, (a, b) -> a)));

        Map<Long, List<ExternalFileDescription>> contestDescriptions = externalFilesRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(ExternalFileDescription::getContestId));

        Map<Long, ContestInfo> contests = contestDescriptions.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, item -> new ContestInfo.Builder(item.getValue()).build()));

        watchers = contests.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, item -> new FileWatcher(item.getValue().getUrlsFromDescriptions())
                        .setContestId(item.getValue().getContestId())
                        .setIsFinalResults(item.getValue().getIsFinal())
                ));
    }

    @Scheduled(fixedDelay = 1000)
    public void updateChanges() {
        watchers.values().forEach(FileWatcher::updateChanges);
    }

    public List<Submission> getLastSubmissions(Long contestId, Long time) {
        return watchers.get(contestId).getContest().getSubmissions()
                .stream()
                .filter(item -> item.getTime() > time)
                .collect(Collectors.toList());
    }

    public Contest getContestData(Long contestId) {
        return watchers.get(contestId).getContest();
    }

    public Map<Long, List<Submission>> getFrozenResults() {
        return watchers.keySet().stream().collect(Collectors.toMap(item -> item, item -> watchers.get(item).getFrozenSubmissions()));
    }
}
