package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.AppRepository;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository endpointHitRepository;
    private final AppRepository applicationRepository;

    public EndpointHitDto createHit(EndpointHitDto endpointHitDto) {
        App app = applicationRepository.findByName(endpointHitDto.getApp())
                .or(saveApp(endpointHitDto))
                .orElseThrow(IllegalStateException::new);

        EndpointHit hit = StatsMapper.fromDtoToHit(endpointHitDto, app);
        EndpointHit createdHit = endpointHitRepository.save(hit);
        return StatsMapper.fromHitToDto(createdHit);
    }

    private Supplier<Optional<? extends App>> saveApp(EndpointHitDto endpointHitDto) {
        return () -> {
            App newApp = new App();
            newApp.setName(endpointHitDto.getApp());
            log.info("App save {}", newApp.getName());
            return Optional.of(applicationRepository.save(newApp));
        };
    }

    @Transactional(readOnly = true)
    public List<ViewStatsDto> getViewStats(ViewStatsRequestDto viewStatsRequestDto) {
        LocalDateTime start = viewStatsRequestDto.getStart();
        LocalDateTime end = viewStatsRequestDto.getEnd();
        List<String> uris = viewStatsRequestDto.getUris();

        if (end.isBefore(start)) {
            throw new BadRequestException("End can't be earlier than start");
        }

        List<ViewStats> viewStatsList;
        if (viewStatsRequestDto.isUnique()) {
            log.info("Stats request from unique IP");
            viewStatsList = endpointHitRepository.findViewStatsByUniqueIp(start, end, uris);
        } else {
            log.info("Stats request from non unique IP");
            viewStatsList = endpointHitRepository.findViewStatsByNonUniqueIp(start, end, uris);
        }
        return StatsMapper.fromListViewStatToDto(viewStatsList);
    }
}
