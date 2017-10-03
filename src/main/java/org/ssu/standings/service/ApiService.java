package org.ssu.standings.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssu.standings.dao.entity.ContestDAO;
import org.ssu.standings.dao.entity.TeamDAO;
import org.ssu.standings.dao.entity.UniversityDAO;
import org.ssu.standings.dao.repository.ContestRepository;
import org.ssu.standings.dao.repository.TeamRepository;
import org.ssu.standings.dao.repository.UniversityRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiService {
    @Resource
    private ContestRepository contestRepository;

    @Resource
    private TeamRepository teamRepository;

    @Resource
    private UniversityRepository universityRepository;

    @Resource
    private StandingsWatchService watchService;

    public List<TeamDAO> teamList() {
        return teamRepository.findAll();
    }

    public List<UniversityDAO> universityList() {
        return universityRepository.findAll();
    }

    public TeamDAO saveTeam(TeamDAO teamDAO) {
        return teamRepository.save(teamDAO);
    }

    public void deleteTeam(Long teamId) {
        teamRepository.delete(teamId);
    }


    public void deleteUniversity(Long universityId) {
        List<TeamDAO> teams = universityRepository.findOne(universityId).getTeamDAOS();
        List<TeamDAO> updatedteamList = teams.stream()
                .map(team -> new TeamDAO.Builder(team).withUniversity(null).build())
                .collect(Collectors.toList());
        teamRepository.save(updatedteamList);

        universityRepository.delete(universityId);
        watchService.initContestDataFlow();
    }

    public UniversityDAO saveUniversity(UniversityDAO universityDAO) {
        return universityRepository.save(universityDAO);
    }

    @Transactional
    public ContestDAO saveContest(ContestDAO contest) {
//        if(contest.getId() != null)
//            contestRepository.delete(contest.getId());
        return contestRepository.save(contest);
//        contestRepository.flush();
//        return contest;
    }

    public List<ContestDAO> contestList() {
        return contestRepository.findAll();
    }

    @Transactional
    public void deleteContest(Long contestId) {
        contestRepository.delete(contestId);
    }
}
