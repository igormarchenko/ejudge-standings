package org.ssu.standings.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.ContestDAO;
import org.ssu.standings.dao.entity.StandingsFileDAO;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.repository.ContestRepository;
import org.ssu.standings.dao.repository.StandingsFilesRepository;
import org.ssu.standings.dao.repository.TeamRepository;
import org.ssu.standings.entity.ContestDataStorage;
import org.ssu.standings.parser.StandingsFileParser;
import org.ssu.standings.updateobserver.ContestStandingsFileObserver;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class StandingsWatchService {
    @Resource
    private TeamRepository teamRepository;

    @Resource
    private StandingsFilesRepository standingsFilesRepository;

    @Resource
    private StandingsFileParser parser;

    @Resource
    private ContestDataStorage contestDataStorage;

    @Resource
    private ContestRepository contestRepository;

    private Map<StandingsFileDAO, ContestStandingsFileObserver> observers = null;

    private Map<Long, ContestDAO> contests = null;

    private void initContestDataFlow() {
        contests = contestRepository.findAll().stream().collect(Collectors.toMap(ContestDAO::getId, Function.identity()));
        observers = standingsFilesRepository.findAll().stream().collect(Collectors.toMap(Function.identity(), ContestStandingsFileObserver::new));

        Map<String, TeamDAO> teams = teamRepository.findAll()
                .stream()
                .collect(Collectors.toMap(TeamDAO::getName, Function.identity(), (existingTeam, newTeam) -> existingTeam));

        contestDataStorage.setTeams(teams);
    }

    @Scheduled(fixedDelay = 1000)
    public void update() {
        if (contests == null) {
            initContestDataFlow();
        }
        observers.entrySet().forEach(observer -> {
            try {
                observer.getValue().update();
                contestDataStorage.updateContest(observer.getKey().getContestId(), parser.parse(observer.getValue().getContent()).orElseThrow(NullPointerException::new), observer.getKey().getFrozen());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
