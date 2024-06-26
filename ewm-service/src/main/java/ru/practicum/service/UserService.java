package ru.practicum.service;


import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto create(UserDto userDto) {
        User user = userMapper.fromDtoToUser(userDto);
        User createUser = userRepository.save(user);

        return userMapper.fromUserToDto(createUser);
    }

    public List<UserDto> getAll(List<Long> id, Pageable pageable) {
        List<User> users;
        if (id != null && !id.isEmpty()) {
            users = userRepository.findAllById(id, pageable);
        } else {
            users = userRepository.findAll(pageable).getContent();
        }

        return userMapper.fromListUsersToDto(users);
    }

    public void deleteById(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User id:" + userId + " not found"));
        userRepository.deleteById(userId);
    }
}
