package org.ssu.standings.service;

import org.springframework.stereotype.Service;
import org.ssu.standings.entity.Team;
import org.ssu.standings.entity.University;
import org.ssu.standings.repository.TeamRepository;
import org.ssu.standings.repository.UniversityRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiService {
    @Resource
    private TeamRepository teamRepository;

    @Resource
    private UniversityRepository universityRepository;

    public List<Team> teamList() {
        return teamRepository.findAll();
    }

    public List<University> universityList() {
        return universityRepository.findAll();
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public void removeTeam(Long teamId) {
        teamRepository.delete(teamId);
    }


}
