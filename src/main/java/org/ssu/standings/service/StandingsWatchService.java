package org.ssu.standings.service;

import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.ssu.standings.dao.entity.*;
import org.ssu.standings.dao.repository.*;
import org.ssu.standings.entity.*;
import org.ssu.standings.parser.*;
import org.ssu.standings.updateobserver.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

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

    private Map<StandingsFileDAO, ContestStandingsFileObserver> observers = null;


    private void initContestDataFlow() {
        observers = standingsFilesRepository.findAll().stream().collect(Collectors.toMap(Function.identity(), ContestStandingsFileObserver::new));

        Map<String, List<TeamDAO>> teams = teamRepository.findAll().stream().collect(Collectors.groupingBy(TeamDAO::getName));
        contestDataStorage.setTeams(teams);
    }

    @Scheduled(fixedDelay = 1000)
    public void update() {
        if (observers == null) {
            initContestDataFlow();
        }
        observers.entrySet().forEach(observer -> {
            try {
                observer.getValue().update();
                contestDataStorage.updateContest(observer.getKey().getContestId(), parser.parse(observer.getValue().getContent()).orElseThrow(NullPointerException::new));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
