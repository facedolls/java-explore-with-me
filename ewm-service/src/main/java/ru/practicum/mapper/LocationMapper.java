package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.LocationDto;
import ru.practicum.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto fromLocationToDto(Location location);

    @Mapping(target = "id", ignore = true)
    Location fromDtoToLocation(LocationDto dto);
}
