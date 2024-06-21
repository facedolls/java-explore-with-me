package ru.practicum;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query(" SELECT new ru.practicum.model.ViewStats(eph.app, eph.uri, COUNT(DISTINCT eph.ip)) " +
            "FROM EndpointHit eph WHERE eph.timestamp BETWEEN ?1 AND ?2 " +
            "AND (eph.uri IN (?3) OR (?3) is NULL) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY COUNT(DISTINCT eph.ip) DESC ")
    List<ViewStats> findUniqueViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, PageRequest pageable);

    @Query(" SELECT new ru.practicum.model.ViewStats(eph.app, eph.uri, COUNT(eph.ip)) " +
            "FROM EndpointHit eph WHERE eph.timestamp BETWEEN ?1 AND ?2 " +
            "AND (eph.uri IN (?3) OR (?3) is NULL) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY COUNT(eph.ip) DESC ")
    List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, PageRequest pageable);
}
