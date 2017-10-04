package org.ssu.standings.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ssu.standings.dao.entity.ContestDAO;
import org.ssu.standings.dao.entity.StandingsFileDAO;
import org.ssu.standings.dao.repository.ContestRepository;
import org.ssu.standings.dao.repository.StandingsFilesRepository;
import org.ssu.standings.entity.ContestDataStorage;
import org.ssu.standings.parser.StandingsFileParser;
import org.ssu.standings.parser.entity.ContestNode;
import org.ssu.standings.updateobserver.ContestStandingsFileObserver;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class StandingsWatchService {


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

    public void initContestDataFlow() {
        contests = contestRepository.findAll().stream().collect(Collectors.toMap(ContestDAO::getId, Function.identity()));
        observers = standingsFilesRepository.findAll().stream().collect(Collectors.toMap(Function.identity(), ContestStandingsFileObserver::new));
        contestDataStorage.updateData();
    }

    @Scheduled(fixedDelay = 1000)
    public void update() {
        if (contests == null) {
            initContestDataFlow();
        }
        Function<List<StandingsFileDAO>, List<ContestNode>> updateContestData = files -> files.stream().map(file -> parser.parse(observers.get(file).update()).orElse(new ContestNode())).collect(Collectors.toList());
        contests.entrySet().forEach(contest ->
                contestDataStorage.updateContest(contest.getKey(),updateContestData.apply(contest.getValue().getStandingsFiles()),contest.getValue().getFinal())
        );
    }
}
