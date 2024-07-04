package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Transactional(readOnly = true)
    public List<CompilationDto> getAllCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        return compilationMapper.fromCompilationListToDto(compilations);
    }

    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = getCompilationOrElseThrow(compId);
        return compilationMapper.fromCompilationToDto(compilation);
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

        Compilation savedCompilation = compilationRepository
                .save(compilationMapper.fromNewDtoToCompilation(newCompilationDto, events));

        return compilationMapper.fromCompilationToDto(savedCompilation);
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

        Compilation updatedCompilation = compilationRepository.save(compilation);

        return compilationMapper.fromCompilationToDto(updatedCompilation);
    }

    public void deleteCompilationById(Long compId) {
        getCompilationById(compId);
        compilationRepository.deleteById(compId);

    }

    private Compilation getCompilationOrElseThrow(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
    }
}
