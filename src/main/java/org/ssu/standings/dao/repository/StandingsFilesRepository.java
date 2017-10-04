package org.ssu.standings.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.dao.entity.StandingsFileDAO;

@Repository
public interface StandingsFilesRepository extends JpaRepository<StandingsFileDAO, Long> {
    void deleteAllByContestId(Long contestId);
}
