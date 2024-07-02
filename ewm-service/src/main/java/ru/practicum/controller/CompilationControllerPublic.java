package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/compilations")
@Validated
@RequiredArgsConstructor
public class CompilationControllerPublic {
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "10";

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = DEFAULT_FROM) @Min(0) Integer from,
                                       @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compilationService.getAllCompilations(pinned, pageable);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }
}
