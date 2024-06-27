package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    public static final String DEFAULT_FROM = "0";
    public static final String DEFAULT_SIZE = "10";

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Create user: {}", userDto);
        return service.create(userDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> id,
                                @RequestParam(defaultValue = DEFAULT_FROM) Integer from,
                                @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return service.getAll(id, pageable);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long userId) {
        log.info("Delete user id: {}", userId);
        service.deleteById(userId);
    }
}
