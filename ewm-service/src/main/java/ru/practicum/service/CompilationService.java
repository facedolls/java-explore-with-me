package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationMapper mapper;
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<CompilationDto> getAllCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations = repository.findAllByPinned(pinned, pageable);
        return mapper.fromCompilationListToDto(compilations);
    }

    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = getCompilationOrElseThrow(compId);
        return mapper.fromCompilationToDto(compilation);
    }

    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Long> eventIds = newCompilationDto.getEvents();
        List<Event> events;

        if (eventIds != null && !eventIds.isEmpty()) {
            events = eventRepository.findAllById(eventIds);
        } else {
            events = new ArrayList<>();
        }

        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }

        Compilation savedCompilation = repository
                .save(mapper.fromNewDtoToCompilation(newCompilationDto, events));

        return mapper.fromCompilationToDto(savedCompilation);
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilationRequest) {
        Compilation compilation = getCompilationOrElseThrow(compId);

        if (compilationRequest.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(compilationRequest.getEvents()));
        }
        Optional.ofNullable(compilationRequest.getTitle())
                .ifPresent(compilation::setTitle);

        Optional.ofNullable(compilationRequest.getPinned())
                .ifPresent(compilation::setPinned);

        Compilation updatedCompilation = repository.save(compilation);

        return mapper.fromCompilationToDto(updatedCompilation);
    }

    public void deleteCompilationById(Long compId) {
        getCompilationById(compId);
        repository.deleteById(compId);

    }

    private Compilation getCompilationOrElseThrow(long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation id " + compId + " not found"));
    }
}
