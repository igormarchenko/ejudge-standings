package org.ssu.standings.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.dao.entity.UserDAO;

@Repository
public interface UserRepository extends JpaRepository<UserDAO, Long> {
}
