package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "category", source = "category")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "id", ignore = true)
    Event fromDtoToEvent(NewEventDto dto, Category category, Location location);

    EventFullDto fromEventToFullDto(Event event);

    List<EventFullDto> fromEventListToFullDto(List<Event> events);

    EventShortDto fromEventToShortDto(Event event);

    List<EventShortDto> fromEventListToShortDto(List<Event> events);
}
