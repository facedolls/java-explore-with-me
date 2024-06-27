package ru.practicum.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final UserRepository repository;

    public UserDto create(UserDto userDto) {
        User user = mapper.fromDtoToUser(userDto);
        User createUser = repository.save(user);

        return mapper.fromUserToDto(createUser);
    }

    public List<UserDto> getAll(List<Long> id, Pageable pageable) {
        List<User> users;
        if (id != null && !id.isEmpty()) {
            users = repository.findAllByIdIn(id, pageable);
        } else {
            users = repository.findAll(pageable).getContent();
        }

        return mapper.fromListUsersToDto(users);
    }

    public void deleteById(Long userId) {
        repository.findById(userId).orElseThrow(() -> new NotFoundException("User id:" + userId + " not found"));
        repository.deleteById(userId);
    }
}
