package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) @NotNull
                                       LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) @NotNull
                                       LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false", required = false) Boolean unique) {
        log.info("GET /stats request for statistics: {}, {}, {}, {}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("POST /hit request to save information that request endpoint {}", endpointHitDto);
        return statsService.createHit(endpointHitDto);
    }
}
