package org.ssu.standings.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.dao.entity.StandingsFile;

import java.util.List;

@Repository
public interface StandingsFilesRepository extends JpaRepository<StandingsFile, Long> {
    List<StandingsFile> findByContestId(Long contestId);

    void deleteByContestId(Long contestId);
}
