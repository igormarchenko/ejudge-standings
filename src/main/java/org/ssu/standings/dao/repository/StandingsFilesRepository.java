package org.ssu.standings.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.dao.entity.StandingsFileDAO;

import java.util.List;

@Repository
public interface StandingsFilesRepository extends JpaRepository<StandingsFileDAO, Long> {
    List<StandingsFileDAO> findByContestId(Long contestId);

    void deleteByContestId(Long contestId);
}
