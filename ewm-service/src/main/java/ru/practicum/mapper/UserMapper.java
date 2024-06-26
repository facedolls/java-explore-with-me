package ru.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    User fromDtoToUser(UserDto userDto);

    UserDto fromUserToDto(User user);

    List<UserDto> fromListUsersToDto(List<User> users);
}
