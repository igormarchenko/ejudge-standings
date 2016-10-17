package org.ssu.standings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssu.standings.entity.Properties;

@Repository
public interface PropertiesRepository extends JpaRepository<Properties, Long> {
    Properties findByKey(String key);
}
