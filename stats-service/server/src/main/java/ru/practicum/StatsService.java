package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.EndTimeBeforeStartTimeException;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatsService {
    private final StatsRepository repository;
    private final StatsMapper mapper;

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        checkTime(start, end);
        List<ViewStats> viewStats;

        if ((uris == null || uris.isEmpty()) || uris.get(0).equals("/events")) {
            viewStats = Boolean.TRUE.equals(unique)
                    ? repository.findAllByDateBetweenAndUniqueIp(start, end)
                    : repository.findAllByDateBetweenStartAndEnd(start, end);
            return mapper.toViewStatsDtoList(viewStats);
        }

        viewStats = Boolean.TRUE.equals(unique)
                ? repository.findAllByDateBetweenAndUriAndUniqueIp(start, end, uris)
                : repository.findAllByDateBetweenAndUri(start, end, uris);

        log.info("GET /stats: visit statistics");
        return mapper.toViewStatsDtoList(viewStats);
    }

    public EndpointHitDto createHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHitSave = repository.save(mapper.toEndpointHit(endpointHitDto));
        log.info("POST create hit {}", endpointHitSave);
        return mapper.toEndpointHitDto(endpointHitSave);
    }

    private void checkTime(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            log.warn("GET /stats: End time cannot be before/equals than start time: {}, {}", start, end);
            throw new EndTimeBeforeStartTimeException("End time cannot be before start time",
                    Collections.singletonList("Incorrect data"));
        }
    }
}
