package ru.practicum;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.AppRepository;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class StatsServiceTest {

    @Mock
    StatsRepository statsRepository;

    @Mock
    AppRepository appRepository;

    @InjectMocks
    StatsService statService;

    @Test
    void createHit() {
        EndpointHitDto hitDto = DataForTests.testEndpointHitDto();
        App app = DataForTests.testApplication();
        EndpointHit hit = DataForTests.testEndpointHit1();
        hit.setApp(app);
        when(appRepository.findByName(any())).thenReturn(Optional.of(app));
        when(statsRepository.save(any(EndpointHit.class)))
                .thenReturn(hit);

        EndpointHitDto resultDto = statService.createHit(hitDto);

        Assertions.assertNotNull(resultDto);
        Assertions.assertEquals(hitDto.getApp(), resultDto.getApp());
        Assertions.assertEquals(hitDto.getUri(), resultDto.getUri());
        Assertions.assertEquals(hitDto.getIp(), resultDto.getIp());

        verify(statsRepository, times(1)).save(any(EndpointHit.class));
    }

    @Test
    void getViewStatsByUniqueIp() {
        ViewStats viewStat = DataForTests.testViewStats();
        ViewStatsRequestDto viewStatsRequestDto = DataForTests.testViewStatsRequestDto();
        when(statsRepository.findViewStatsByUniqueIp(any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(List.of(viewStat));

        List<ViewStatsDto> result = statService.getViewStats(viewStatsRequestDto);

        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getViewStatsByNonUniqueIp() {
        ViewStats viewStat = DataForTests.testViewStats();
        ViewStatsRequestDto viewStatsRequestDto = DataForTests.testViewStatsRequestDto();
        viewStatsRequestDto.setUnique(false);
        when(statsRepository.findViewStatsByNonUniqueIp(any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(List.of(viewStat));

        List<ViewStatsDto> result = statService.getViewStats(viewStatsRequestDto);

        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getViewStatsWithoutUris() {
        ViewStats viewStat = DataForTests.testViewStats();
        ViewStatsRequestDto viewStatsRequestDto = DataForTests.testViewStatsRequestDto2();
        when(statsRepository.findViewStatsByNonUniqueIp(any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(List.of(viewStat));

        List<ViewStatsDto> result = statService.getViewStats(viewStatsRequestDto);

        Assertions.assertEquals(1, result.size());
    }
}
