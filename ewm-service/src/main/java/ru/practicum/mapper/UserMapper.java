package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto fromUserToDto(User user);

    List<UserDto> fromListUsersToDto(List<User> users);

    User fromDtotoUser(UserDto userDto);

    UserShortDto fromUserToUserShortDto(User user);
}
