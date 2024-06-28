package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.AdminEventUpdateRequest;
import ru.practicum.dto.AdminRequestParamDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.model.EventState;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerAdmin {
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "10";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final EventService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<EventState> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime rangeStart,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime rangeEnd,
                                     @RequestParam(defaultValue = DEFAULT_FROM) @Min(0) Integer from,
                                     @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        log.info("Admin event request ");
        AdminRequestParamDto requestParamDto = new AdminRequestParamDto(users, states, categories,
                rangeStart, rangeEnd);
        Pageable pageable = PageRequest.of(from / size, size);
        return service.getAllByAdmin(requestParamDto, pageable);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@Valid @RequestBody AdminEventUpdateRequest adminRequest, @PathVariable Long eventId) {
        log.info("Admin event update {}", adminRequest);
        return service.updateByAdmin(adminRequest, eventId);
    }
}
