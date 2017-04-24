package org.ssu.standings.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.repository.StandingsFilesRepository;
import org.ssu.standings.entity.ContestInfo;
import org.ssu.standings.dao.entity.StandingsFileDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.dao.repository.TeamRepository;
import org.ssu.standings.dao.repository.UniversityRepository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApiService {
    @Resource
    private StandingsFilesRepository standingsFilesRepository;

    @Resource
    private TeamRepository teamRepository;

    @Resource
    private UniversityRepository universityRepository;

    public List<TeamDAO> teamList() {
        return teamRepository.findAll();
    }

    public List<UniversityDAO> universityList() {
        return universityRepository.findAll();
    }

    public TeamDAO saveTeam(TeamDAO teamDAO) {
        return teamRepository.save(teamDAO);
    }

    public void removeTeam(Long teamId) {
        teamRepository.delete(teamId);
    }


    public void removeUniversity(Long universityId) {
        universityRepository.delete(universityId);
    }

    public UniversityDAO saveUniversity(UniversityDAO universityDAO) {
        return universityRepository.save(universityDAO);
    }

    public List<ContestInfo> contestList() {
        Map<Long, List<StandingsFileDAO>> externalFiles = standingsFilesRepository.findAll().stream()
                .collect(Collectors.groupingBy(StandingsFileDAO::getContestId));

        return externalFiles.entrySet().stream()
                .map(item -> new ContestInfo.Builder(item.getKey(), item.getValue().get(0).getIsFinal(), item.getValue()).build())
                .collect(Collectors.toList());
    }

    private Long lastContestId() {
        Optional<StandingsFileDAO> lastContest = standingsFilesRepository.findAll().stream().sorted(Comparator.comparingLong(StandingsFileDAO::getContestId)).reduce((a, b) -> b);
        return lastContest.map(StandingsFileDAO::getContestId).orElse(0L);
    }

    @Transactional
    public ContestInfo saveContest(ContestInfo contestInfo) {
        Long contestId = Optional.ofNullable(contestInfo.getContestId()).orElse(lastContestId() + 1);
        Boolean isFinal = Optional.ofNullable(contestInfo.getIsFinal()).orElse(false);

        deleteContest(contestInfo.getContestId());

        contestInfo = new ContestInfo.Builder()
                .withContestId(contestId)
                .withExternalFileDesriptions(contestInfo.getStandingsFileDAOS().stream()
                        .filter(Objects::nonNull)
                        .map(item -> new StandingsFileDAO.Builder(item)
                                .withContestId(contestId)
                                .withIsFinal(isFinal)
                                .build())
                        .collect(Collectors.toList()))
                .withIsFinal(contestInfo.getIsFinal()).build();

        return new ContestInfo.Builder(contestInfo.getContestId(),
                contestInfo.getIsFinal(),
                standingsFilesRepository.save(contestInfo.getStandingsFileDAOS().stream().filter(Objects::nonNull).collect(Collectors.toList()))).build();
    }

    @Transactional
    public void deleteContest(Long contestId) {
        standingsFilesRepository.deleteByContestId(contestId);
    }
}
