package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.participationRequest.ParticipationRequest;
import ru.practicum.model.participationRequest.ParticipationState;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestMapper requestMapper;

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getAllRequestsByRequester(Long userId) {
        getUserOrElseThrow(userId);
        return requestMapper.fromListRequestToDto(requestRepository.findAllByRequesterId(userId));
    }

    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User requester = getUserOrElseThrow(userId);
        Event event = getEventOrElseThrow(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Request can't be executed by initiator");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Request can't be executed for unpublished event");
        }

        if (event.getParticipantLimit() > 0) {
            if (requestRepository.getCountByEventIdAndStatus(eventId, ParticipationState.CONFIRMED) >= event.getParticipantLimit()) {
                throw new ConflictException("Limit of participants reached");
            }
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());

        if (event.getParticipantLimit() != 0 && event.getRequestModeration()) {
            participationRequest.setStatus(ParticipationState.PENDING);
        } else {
            participationRequest.setStatus(ParticipationState.CONFIRMED);
        }
        ParticipationRequest savedRequest = requestRepository.save(participationRequest);

        return requestMapper.fromRequestToDto(savedRequest);
    }

    public ParticipationRequestDto cancelRequestByRequester(Long userId, Long requestId) {
        getUserOrElseThrow(userId);
        ParticipationRequest request = getRequestOrElseThrow(requestId);

        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException("Request for user not found");
        }

        request.setStatus(ParticipationState.CANCELED);

        ParticipationRequest canceledRequest = requestRepository.save(request);

        return requestMapper.fromRequestToDto(canceledRequest);
    }

    private ParticipationRequest getRequestOrElseThrow(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ParticipationRequest id " + requestId + " not found"));
    }

    private Event getEventOrElseThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id " + eventId + " not found"));
    }

    private User getUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id " + userId + " not found"));
    }
}
