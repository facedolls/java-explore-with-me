package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHit;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {
    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        PageRequest pageable = PageRequest.of(0, 10);
        if (Boolean.TRUE.equals(unique)) {
            return statsMapper.toViewStatsDtoList(statsRepository.findUniqueViewStats(start, end, uris, pageable));
        } else {
            return statsMapper.toViewStatsDtoList(statsRepository.findViewStats(start, end, uris, pageable));
        }
    }

    @Transactional
    public EndpointHitDto createHit(EndpointHitDto hit) {
        EndpointHit endpointHitSave = statsRepository.save(statsMapper.toEndpointHit(hit));
        log.info("POST /hit: create hit={}", endpointHitSave);
        return statsMapper.toEndpointHitDto(endpointHitSave);
    }
}
