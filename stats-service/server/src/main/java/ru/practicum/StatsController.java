package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.BadRequestException;

import javax.validation.Valid;
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
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam
                                                       @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                                       @RequestParam
                                                       @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                                       @RequestParam(required = false) List<String> uris,
                                                       @RequestParam(defaultValue = "false") Boolean unique) {

        if (start.isAfter(end)) {
            throw new BadRequestException("Start date is older then end date");
        }
        log.info("GET /stats: start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return new ResponseEntity<>(statsService.getStats(start, end, uris, unique), HttpStatus.OK);
    }

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> createHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("POST /hit: endpoint={}", endpointHitDto);
        return new ResponseEntity<>(statsService.createHit(endpointHitDto), HttpStatus.CREATED);
    }
}
