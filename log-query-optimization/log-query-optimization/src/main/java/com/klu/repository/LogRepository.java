package com.klu.repository;

import com.klu.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByMessageContainingIgnoreCase(String keyword);

    List<Log> findByLevel(String level);

    List<Log> findByService(String service);

    List<Log> findByLevelAndService(String level, String service);
}	