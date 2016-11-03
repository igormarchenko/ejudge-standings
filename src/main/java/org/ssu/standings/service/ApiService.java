package org.ssu.standings.service;

import org.springframework.stereotype.Service;
import org.ssu.standings.entity.Team;
import org.ssu.standings.repository.TeamRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiService {
    @Resource
    private TeamRepository teamRepository;

    public List<Team> teamList() {
        return teamRepository.findAll();
    }
}
