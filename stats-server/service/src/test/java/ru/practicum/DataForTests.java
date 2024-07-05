package ru.practicum;


import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class DataForTests {
    private static final String ipTestOne = "127.0.0.1";
    private static final String appTest = "ewm-main-service";
    private static final String uriOne = "/events/1";
    private static final String uriTree = "/events/3";
    private static final LocalDateTime time = LocalDateTime.now().withNano(0);

    public static EndpointHitDto testEndpointHitDto() {
        return EndpointHitDto.builder()
                .app(appTest)
                .uri(uriOne)
                .ip(ipTestOne)
                .time(time)
                .build();
    }

    public static ViewStatsDto testViewStatsDto() {
        return ViewStatsDto.builder()
                .app(appTest)
                .uri(uriOne)
                .hits(1L)
                .build();
    }

    public static App testApplication() {
        App application = new App();
        application.setName(appTest);
        return application;
    }

    public static ViewStats testViewStats() {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(testApplication());
        viewStats.setUri(uriOne);
        viewStats.setHits(1L);
        return viewStats;
    }

    public static EndpointHit testEndpointHit1() {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setId(1L);
        endpointHit.setUri(uriOne);
        endpointHit.setIp(ipTestOne);
        endpointHit.setTimestamp(time);
        return endpointHit;
    }

    public static ViewStatsRequestDto testViewStatsRequestDto() {
        return new ViewStatsRequestDto(time.minusYears(2),
                time.plusYears(2), List.of(uriOne), true);
    }

    public static ViewStatsRequestDto testViewStatsRequestDto2() {
        return new ViewStatsRequestDto(time.minusYears(2),
                time.plusYears(2), Collections.emptyList(), false);
    }
}
