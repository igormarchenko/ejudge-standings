package org.ssu.standings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.entity.ExternalFileDescription;

import java.util.List;

@Repository
public interface ExternalFilesRepository extends JpaRepository<ExternalFileDescription, Long>{
    List<ExternalFileDescription> findByContestId(Long contestId);
}
