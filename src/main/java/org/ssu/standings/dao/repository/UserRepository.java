package org.ssu.standings.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.dao.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
