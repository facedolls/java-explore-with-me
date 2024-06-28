package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.InvalidTimeException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventService {
    private final EventMapper mapper;
    private final LocationMapper locationMapper;
    private final ParticipationRequestMapper participationMapper;
    private final EventRepository repository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRepository;
    private final StatsClient statClient;

    public List<EventFullDto> getAllByAdmin(AdminRequestParamDto requestParamDto, Pageable pageable) {
        List<Long> userIds = requestParamDto.getUserIds();
        List<EventState> states = requestParamDto.getStates();
        List<Long> categoryIds = requestParamDto.getCategoryIds();
        LocalDateTime rangeStart = requestParamDto.getRangeStart();
        LocalDateTime rangeEnd = requestParamDto.getRangeEnd();

        if (userIds != null && userIds.size() == 1 && userIds.get(0).equals(0L)) {
            userIds = null;
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = rangeStart.plusDays(7);
        }

        List<Event> events = repository.findAllByAdmin(userIds, states, categoryIds, rangeStart, rangeEnd, pageable);
        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Map<String, Long> views = getViewsByEvents(events);

        List<ParticipationRequest> allRequests = participationRepository.findAllByEventIdInAndStatus(eventIds, ParticipationState.CONFIRMED);
        Map<Long, Long> countRequestsByEventId = allRequests.stream()
                .collect(Collectors.groupingBy(r -> r.getEvent().getId(), Collectors.counting()));

        List<EventFullDto> fullDtoList = mapper.fromEventListToFullDto(events);
        fullDtoList.forEach(eventFullDto -> {
            eventFullDto.setConfirmedRequests(countRequestsByEventId.getOrDefault(eventFullDto.getId(), 0L));
            eventFullDto.setViews(views.getOrDefault("/events/" + eventFullDto.getId(), 0L));
        });

        return fullDtoList;
    }

    public List<EventShortDto> getAllByInitiator(Long userId, Pageable pageable) {
        List<Event> events = repository.findAllByInitiatorId(userId, pageable);
        return mapper.fromEventListToShortDto(events);
    }

    public EventFullDto getByIdByInitiator(Long userId, Long eventId) {
        Event event = getEventOrElseThrow(eventId);
        checkEventForUser(userId, eventId);
        return mapper.fromEventToFullDto(event);
    }

    public List<ParticipationRequestDto> getRequestsByEventId(Long userId, Long eventId) {
        getEventOrElseThrow(eventId);
        getUserOrElseThrow(userId);
        checkEventForUser(userId, eventId);
        List<ParticipationRequest> requests = participationRepository.findAllByEventId(eventId);
        return participationMapper.fromListRequestToDto(requests);
    }

    public List<EventShortDto> getAllPublishedEvents(PublicRequestParamDto paramDto, Pageable pageable) {
        String text = paramDto.getText();
        List<Long> categories = paramDto.getCategories();
        Boolean paid = paramDto.getPaid();
        LocalDateTime rangeStart = paramDto.getRangeStart();
        LocalDateTime rangeEnd = paramDto.getRangeEnd();
        boolean onlyAvailable = paramDto.isOnlyAvailable();
        EventSort sort = paramDto.getSort();
        HttpServletRequest request = paramDto.getRequest();

        registerEndpointHit(request);

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Start can't be after end");
        }

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusDays(14);
        }

        if (categories != null && categories.size() == 1 && categories.get(0).equals(0L)) {
            categories = null;
        }

        List<Event> events = repository.getAllPublicByParams(text, categories, paid, rangeStart, rangeEnd, pageable);

        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit().equals(0)
                            || (participationRepository.getCountByEventIdAndStatus(event.getId(), ParticipationState.CONFIRMED)
                            < event.getParticipantLimit()))
                    .collect(Collectors.toList());
        }
        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Map<String, Long> views = getViewsByEvents(events);

        List<ParticipationRequest> allRequests = participationRepository.findAllByEventIdInAndStatus(eventIds, ParticipationState.CONFIRMED);
        Map<Long, Long> countRequestsByEventId = allRequests.stream()
                .collect(Collectors.groupingBy(r -> r.getEvent().getId(), Collectors.counting()));

        List<EventShortDto> shortDtoList = mapper.fromEventListToShortDto(events);
        shortDtoList.forEach(eventShortDto -> {
            eventShortDto.setConfirmedRequests(countRequestsByEventId.getOrDefault(eventShortDto.getId(), 0L));
            eventShortDto.setViews(views.getOrDefault("/events/" + eventShortDto.getId(), 0L));
        });

        switch (sort) {
            case EVENT_DATE:
                shortDtoList.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                shortDtoList.sort(Comparator.comparing(EventShortDto::getViews).reversed());
                break;
        }

        return shortDtoList;
    }

    public EventFullDto getPublishedEventById(Long eventId, HttpServletRequest request) {
        Event event = getEventOrElseThrow(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        registerEndpointHit(request);

        EventFullDto fullDto = mapper.fromEventToFullDto(event);
        Map<String, Long> views = getViewsByEvents(Collections.singletonList(event));
        fullDto.setViews(views.getOrDefault("/events/" + fullDto.getId(), 0L));

        Long limit = participationRepository.getCountByEventIdAndStatus(eventId, ParticipationState.CONFIRMED);
        fullDto.setConfirmedRequests(limit);

        return fullDto;
    }

    public EventFullDto createByInitiator(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidTimeException("Time for which event is scheduled cannot be earlier " +
                    "than two hours from the current");
        }
        User initiator = getUserOrElseThrow(userId);
        Category category = getCategoryOrElseThrow(newEventDto.getCategory());
        Location location = getOrCreateLocation(newEventDto.getLocation());
        Event event = mapper.fromDtoToEvent(newEventDto, category, location);

        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }

        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }

        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        Event savedEvent = repository.save(event);
        return mapper.fromEventToFullDto(savedEvent);
    }

    public EventFullDto updateByAdmin(AdminEventUpdateRequest adminRequest, Long eventId) {
        Event event = getEventOrElseThrow(eventId);
        checkEventStateConflictsForAdmin(adminRequest, event);

        updateCommonEventFields(event, adminRequest.getCategory(), adminRequest.getLocation(),
                adminRequest.getAnnotation(), adminRequest.getTitle(), adminRequest.getDescription(),
                adminRequest.getParticipantLimit(), adminRequest.getEventDate(), adminRequest.getPaid(),
                adminRequest.getRequestModeration());

        if (adminRequest.getStateAction() != null) {
            switch (adminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        Event updatedEvent = repository.save(event);
        return mapper.fromEventToFullDto(updatedEvent);
    }

    public EventFullDto updateByInitiator(Long userId, Long eventId, UserEventUpdateRequest userRequest) {
        Event event = getEventOrElseThrow(eventId);
        checkEventForUser(userId, eventId);
        checkEventStateConflictsForInitiator(userRequest, event);

        updateCommonEventFields(event, userRequest.getCategory(), userRequest.getLocation(),
                userRequest.getAnnotation(), userRequest.getTitle(), userRequest.getDescription(),
                userRequest.getParticipantLimit(), userRequest.getEventDate(), userRequest.getPaid(),
                userRequest.getRequestModeration());

        if (userRequest.getStateAction() != null) {
            switch (userRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        Event updatedEvent = repository.save(event);
        return mapper.fromEventToFullDto(updatedEvent);
    }

    private void updateCommonEventFields(Event event, Long category, LocationDto location, String annotation,
                                         String title, String description, Integer participantLimit,
                                         LocalDateTime eventDate, Boolean paid, Boolean requestModeration) {
        Optional.ofNullable(category)
                .map(this::getCategoryOrElseThrow)
                .ifPresent(event::setCategory);
        Optional.ofNullable(location)
                .ifPresent(dto -> event.setLocation(getOrCreateLocation(dto)));
        Optional.ofNullable(annotation)
                .ifPresent(event::setAnnotation);
        Optional.ofNullable(title)
                .ifPresent(event::setTitle);
        Optional.ofNullable(description)
                .ifPresent(event::setDescription);
        Optional.ofNullable(participantLimit)
                .ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventDate)
                .ifPresent(event::setDate);
        Optional.ofNullable(paid)
                .ifPresent(event::setPaid);
        Optional.ofNullable(requestModeration)
                .ifPresent(event::setRequestModeration);
    }

    public EventRequestStatusUpdateResult updateStatusOfRequests(Long userId, Long eventId,
                                                                 EventRequestStatusUpdateRequest statusUpdateRequest) {

        Event event = getEventOrElseThrow(eventId);
        getUserOrElseThrow(userId);

        Long amountOfParticipants = participationRepository.getCountByEventIdAndStatus(eventId, ParticipationState.CONFIRMED);
        Long availableLimit = event.getParticipantLimit() - amountOfParticipants;
        if (availableLimit <= 0) {
            throw new ConflictException("Participant limit reached");
        }

        List<Long> requestIds = statusUpdateRequest.getRequestIds();
        List<ParticipationRequest> requests = participationRepository.findAllByIdIn(requestIds);

        EventRequestStatusUpdateResult statusUpdateResult = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
            return statusUpdateResult;
        }

        for (ParticipationRequest request : requests) {
            if (!request.getEvent().getId().equals(eventId)) {
                throw new NotFoundException("Request id " + request.getId() + " not found");
            }
            if (!request.getStatus().equals(ParticipationState.PENDING)) {
                throw new BadRequestException("Request should have status PENDING");
            }
            if (availableLimit <= 0) {
                request.setStatus(ParticipationState.REJECTED);
                statusUpdateResult.getRejectedRequests().add(participationMapper.fromRequestToDto(request));
            }
            switch (statusUpdateRequest.getStatus()) {
                case CONFIRMED:
                    request.setStatus(ParticipationState.CONFIRMED);
                    statusUpdateResult.getConfirmedRequests().add(participationMapper.fromRequestToDto(request));
                    availableLimit--;
                    break;
                case REJECTED:
                    request.setStatus(ParticipationState.REJECTED);
                    statusUpdateResult.getRejectedRequests().add(participationMapper.fromRequestToDto(request));
                    break;
            }
        }
        participationRepository.saveAll(requests);

        return statusUpdateResult;
    }

    private Location getOrCreateLocation(LocationDto locationDto) {
        float lat = locationDto.getLatitude();
        float lon = locationDto.getLongitude();
        Location location = locationRepository.findByLatAndLon(lat, lon);
        if (location == null) {
            location = locationRepository.save(locationMapper.fromDtoToLocation(locationDto));
        }
        return location;
    }

    private void checkEventForUser(Long userId, Long eventId) {
        Event event = getEventOrElseThrow(eventId);
        Long initiatorId = event.getInitiator().getId();
        if (initiatorId != userId) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
    }

    private Category getCategoryOrElseThrow(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    private Event getEventOrElseThrow(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private User getUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private void checkEventStateConflictsForAdmin(AdminEventUpdateRequest adminRequest, Event event) {
        if (adminRequest.getEventDate() != null) {
            if (adminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ConflictException("Start date of event modified must be no earlier " +
                        "than one hour from date of publication");
            }
        }

        if (adminRequest.getStateAction() != null) {
            if (!event.getState().equals(EventState.PENDING)
                    && adminRequest.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
                throw new ConflictException("Event can be published only if it is in waiting state for publication");
            }
            if (event.getState().equals(EventState.PUBLISHED)
                    && adminRequest.getStateAction().equals(AdminStateAction.REJECT_EVENT)) {
                throw new ConflictException("Event can be rejected only if it has not been published yet");
            }
        }
    }

    private void checkEventStateConflictsForInitiator(UserEventUpdateRequest userRequest, Event event) {
        if (userRequest.getEventDate() != null && userRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Time for which event is scheduled cannot be earlier " +
                    "than two hours from the current moment");
        }

        boolean isNotAllowed = !(event.getState().equals(EventState.PENDING)
                || event.getState().equals(EventState.CANCELED));

        if (isNotAllowed) {
            throw new ConflictException("Can only change canceled or events in waiting for moderation state");
        }
    }

    private void registerEndpointHit(HttpServletRequest request) {
        String app = "ewm-main-service";
        statClient.createHit(EndpointHitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    private Map<String, Long> getViewsByEvents(List<Event> events) {

        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        Optional<LocalDateTime> startDate = events.stream().map(Event::getPublishedOn)
                .filter(Objects::nonNull).min(LocalDateTime::compareTo);
        if (startDate.isPresent()) {
            start = startDate.get();
        } else {
            return new HashMap<>();
        }

        List<String> eventUris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        return statClient.getViews(start, end, eventUris).stream()
                .collect(Collectors.toMap(ViewStatsDto::getUri, ViewStatsDto::getHits));
    }
}
