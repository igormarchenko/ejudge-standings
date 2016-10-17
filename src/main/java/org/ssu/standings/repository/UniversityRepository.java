package org.ssu.standings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.entity.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long>{
}
