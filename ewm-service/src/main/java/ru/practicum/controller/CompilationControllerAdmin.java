package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class CompilationControllerAdmin {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Creat events compilation {}", newCompilationDto);
        return service.createCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest compilationRequest) {
        log.info("Update events compilation {}", compilationRequest);
        return service.updateCompilation(compId, compilationRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long compId) {
        log.info("Delete events compilation by id {}", compId);
        service.deleteCompilationById(compId);
    }
}
