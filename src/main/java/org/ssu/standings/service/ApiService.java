package org.ssu.standings.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssu.standings.dao.entity.ContestDAO;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.repository.ContestRepository;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.dao.repository.TeamRepository;
import org.ssu.standings.dao.repository.UniversityRepository;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ApiService {
    @Resource
    private ContestRepository contestRepository;

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

    public List<ContestDAO> contestList() {
        return contestRepository.findAll();
    }

    private Long lastContestId() {
//        Optional<StandingsFileDAO> lastContest = standingsFilesRepository.findAll().stream().sorted(Comparator.comparingLong(StandingsFileDAO::getContestId)).reduce((a, b) -> b);
//        return lastContest.map(StandingsFileDAO::getContestId).orElse(0L);
        return -1L;
    }

//    @Transactional
//    public ContestInfo saveContest(ContestInfo contestInfo) {
//        Long contestId = Optional.ofNullable(contestInfo.getContestId()).orElse(lastContestId() + 1);
//        Boolean isFinal = Optional.ofNullable(contestInfo.getIsFinal()).orElse(false);
//
//        deleteContest(contestInfo.getContestId());
//
//        contestInfo = new ContestInfo.Builder()
//                .withContestId(contestId)
//                .withExternalFileDesriptions(contestInfo.getStandingsFileDAOS().stream()
//                        .filter(Objects::nonNull)
//                        .map(item -> new StandingsFileDAO.Builder(item)
//                                .withContestId(contestId)
//                                .withIsFinal(isFinal)
//                                .build())
//                        .collect(Collectors.toList()))
//                .withIsFinal(contestInfo.getIsFinal()).build();
//
//        return new ContestInfo.Builder(contestInfo.getContestId(),
//                contestInfo.getIsFinal(),
//                standingsFilesRepository.save(contestInfo.getStandingsFileDAOS().stream().filter(Objects::nonNull).collect(Collectors.toList()))).build();
//        return null;
//    }

    @Transactional
    public void deleteContest(Long contestId) {
        contestRepository.delete(contestId);
    }
}
