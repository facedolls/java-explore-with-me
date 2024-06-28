package ru.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "location", source = "location")
    Event fromDtoToEvent(NewEventDto dto, Category category, Location location);

    EventFullDto fromEventToFullDto(Event event);

    List<EventFullDto> fromEventListToFullDto(List<Event> events);

    EventShortDto fromEventToShortDto(Event event);

    List<EventShortDto> fromEventListToShortDto(List<Event> events);
}
