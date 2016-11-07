package org.ssu.standings.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ssu.standings.entity.Contest;
import org.ssu.standings.entity.Submission;
import org.ssu.standings.entity.Team;
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
        TeamInUniversityList.setTeamUniversity(teamRepository.findAll().stream().collect(Collectors.toMap(item -> item.getName().trim(), Team::getUniversityEntity, (a, b) -> a)));

     /*   watchers = externalFilesRepository.findAll()
                .stream()
                .map(item -> new FileWatcher(item.getLink())
                        .setContestId(item.getContestId())
                        .setIsFinalResults(item.getFinal()))
                .collect(Collectors.toMap(FileWatcher::getContestId, item -> item));*/
    }

    @Scheduled(fixedDelay = 1000)
    public void checkChanges() {
        watchers.values().forEach(FileWatcher::isChanged);
    }

    public Map<Long, List<Submission>> getLastSubmissions(Map<Long, Long> lastSubmits) {
        return lastSubmits.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, item -> watchers.get(item.getKey()).getLastChanged(item.getValue())));
    }

    public Boolean isContestChanged(Map<Long, Long> lastSubmit) {
        return !getLastSubmissions(lastSubmit).isEmpty();
    }

    public List<Contest> getContestData() {
        return watchers.values().stream().map(FileWatcher::getContest).collect(Collectors.toList());
    }

    public Map<Long, List<Submission>> getFrozenResults() {
        return watchers.keySet().stream().collect(Collectors.toMap(item -> item, item -> watchers.get(item).getFrozenSubmissions()));
    }
}
