package ru.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.dto.LocationDto;
import ru.practicum.model.Location;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LocationMapper {
    LocationDto fromLocationToDto(Location location);

    Location fromDtoToLocation(LocationDto dto);
}
