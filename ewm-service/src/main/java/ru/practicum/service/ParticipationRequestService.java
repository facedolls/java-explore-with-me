package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.repository.ParticipationRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationRequestService {
    private final ParticipationRequestMapper mapper;
    private final ParticipationRequestRepository repository;

    public ParticipationRequestDto create(Long userId, Long eventId) {
        return null;
    }

    public List<ParticipationRequestDto> getAll(Long userId) {
        return null;
    }

    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        return null;
    }
}