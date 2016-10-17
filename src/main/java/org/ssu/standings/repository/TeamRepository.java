package org.ssu.standings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.entity.Team;

@Repository
public interface TeamRepository  extends JpaRepository<Team, Long>{
    Team findByName(String name);
}
