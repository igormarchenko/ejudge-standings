package org.ssu.standings.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.dao.entity.TeamDAO;

@Repository
public interface TeamRepository extends JpaRepository<TeamDAO, Long> {
    TeamDAO findByName(String name);
}
