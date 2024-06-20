package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final StatsService statsService;

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT)
                                                       @NotBlank LocalDateTime start,
                                                       @RequestParam @DateTimeFormat(pattern = DATE_FORMAT)
                                                       @NotBlank LocalDateTime end,
                                                       @RequestParam List<String> uris,
                                                       @RequestParam(defaultValue = "false") boolean unique) {
        log.info("GET /stats: start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return new ResponseEntity<>(statsService.getStats(start, end, uris, unique), HttpStatus.OK);
    }

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> createHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("POST /hit: endpoint={}", endpointHitDto);
        return new ResponseEntity<>(statsService.createHit(endpointHitDto), HttpStatus.CREATED);
    }
}
