package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.ParticipationRequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class ParticipationRequestController {
    private final ParticipationRequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAll(@PathVariable Long userId) {
        return requestService.getAllRequestsByRequester(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Create request event {} from user: {}", eventId, userId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Cancel request: {}", requestId);
        return requestService.cancelRequestByRequester(userId, requestId);
    }
}
