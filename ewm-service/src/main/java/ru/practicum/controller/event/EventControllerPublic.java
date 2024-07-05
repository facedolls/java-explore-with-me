package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.PublicRequestParamDto;
import ru.practicum.model.event.EventSort;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "10";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAll(@RequestParam(defaultValue = "") String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime rangeStart,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                      @RequestParam(defaultValue = "VIEWS") EventSort sort,
                                      @RequestParam(defaultValue = DEFAULT_FROM) @Min(0) Integer from,
                                      @RequestParam(defaultValue = DEFAULT_SIZE) Integer size,
                                      HttpServletRequest request) {

        PublicRequestParamDto requestParamDto = new PublicRequestParamDto(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, request);

        Pageable pageable = PageRequest.of(from / size, size);
        return eventService.getAllPublishedEvents(requestParamDto, pageable);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable Long id,
                                HttpServletRequest request) {
        return eventService.getPublishedEventById(id, request);
    }
}
