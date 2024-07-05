package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsController {
    private final StatsService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("POST /hit request to save information that request endpoint {}", endpointHitDto);
        statService.createHit(endpointHitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getViewStats(@RequestParam LocalDateTime start,
                                           @RequestParam LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        if (uris != null && !uris.isEmpty()) {
            List<String> cleanedUris = new ArrayList<>();
            for (String uri : uris) {
                cleanedUris.add(uri.replaceAll("[\\[\\]]", ""));
            }
            uris = cleanedUris;
        }

        ViewStatsRequestDto viewStatsRequestDto = new ViewStatsRequestDto(
                start, end, uris, unique
        );
        log.info("GET /stats request for statistics {}", viewStatsRequestDto);
        return statService.getViewStats(viewStatsRequestDto);
    }
}
