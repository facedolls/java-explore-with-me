package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.model.ViewStats(eph.app, eph.uri, COUNT(eph.ip)) FROM EndpointHit AS eph " +
            "WHERE (eph.timestamp BETWEEN :start AND :end) AND eph.uri IN (:uris) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY COUNT(eph.ip) DESC")
    List<ViewStats> findAllByDateBetweenAndUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(eph.app, eph.uri, COUNT(DISTINCT eph.ip)) FROM EndpointHit AS eph " +
            "WHERE (eph.timestamp BETWEEN :start AND :end) AND eph.uri IN (:uris) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY COUNT(DISTINCT eph.ip) DESC")
    List<ViewStats> findAllByDateBetweenAndUriAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(eph.app, eph.uri, COUNT(eph.ip)) FROM EndpointHit AS eph " +
            "WHERE (eph.timestamp BETWEEN :start AND :end) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY COUNT(eph.ip) DESC")
    List<ViewStats> findAllByDateBetweenStartAndEnd(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(eph.app, eph.uri, COUNT(DISTINCT eph.ip)) FROM EndpointHit AS eph " +
            "WHERE (eph.timestamp BETWEEN :start AND :end) " +
            "GROUP BY eph.app, eph.uri " +
            "ORDER BY COUNT(DISTINCT eph.ip) DESC")
    List<ViewStats> findAllByDateBetweenAndUniqueIp(LocalDateTime start, LocalDateTime end);
}
