package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.dto.participationRequest.UserEventUpdateRequest;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "10";

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAll(@PathVariable Long userId,
                                      @RequestParam(defaultValue = DEFAULT_FROM) @Min(0) Integer from,
                                      @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventService.getAllByInitiator(userId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable @NotNull @Min(1L) Long userId,
                               @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Event creation by initiator {}", newEventDto);
        return eventService.createByInitiator(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        return eventService.getByIdByInitiator(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UserEventUpdateRequest userRequest) {
        log.info("Event update by initiator {}", userRequest);
        return eventService.updateByInitiator(userId, eventId, userRequest);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsByEventId(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return eventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateStatusOfRequests(@PathVariable Long userId,
                                                                 @PathVariable Long eventId,
                                                                 @Valid @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("Updating status of participation requests {}", statusUpdateRequest);
        return eventService.updateStatusOfRequests(userId, eventId, statusUpdateRequest);
    }
}
