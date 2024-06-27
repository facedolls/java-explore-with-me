package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CategoryControllerPublic {
    public static final String DEFAULT_FROM = "0";
    public static final String DEFAULT_SIZE = "10";

    private final CategoryService service;

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable Long catId) {
        return service.get(catId);
    }

    @GetMapping
    @ResponseStatus
    public List<CategoryDto> getAll(@RequestParam(defaultValue = DEFAULT_FROM) @Min(0) Integer from,
                                    @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return service.getAll(pageable);
    }
}
