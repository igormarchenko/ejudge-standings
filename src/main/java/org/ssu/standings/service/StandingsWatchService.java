package org.ssu.standings.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ssu.standings.dao.entity.StandingsFileDAO;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.repository.StandingsFilesRepository;
import org.ssu.standings.dao.repository.TeamRepository;
import org.ssu.standings.entity.Contest;
import org.ssu.standings.entity.ContestStandingsFileObserver;
import org.ssu.standings.parser.StandingsFileParser;
import org.ssu.standings.parser.entity.ContestNode;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class StandingsWatchService {
    @Resource
    private TeamRepository teamRepository;

    @Resource
    private StandingsFilesRepository standingsFilesRepository;

    @Resource
    private StandingsFileParser parser;

    private Map<StandingsFileDAO, ContestStandingsFileObserver> observers;

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

    public Contest getContestData(Long contestId) {
        Map<String, List<TeamDAO>> teams = teamRepository.findAll().stream().collect(Collectors.groupingBy(TeamDAO::getName));
        List<ContestNode> contestNodes = observers.entrySet()
                .stream()
                .filter(observer -> observer.getKey().getContestId().equals(contestId))
                .map(observer -> parser.parse(observer.getValue().getContent()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return contestNodes.stream().map(node -> new Contest.Builder(node, teams).build()).collect(Collectors.toList()).get(0);
    }
}
