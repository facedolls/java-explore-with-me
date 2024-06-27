package ru.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ParticipationRequestMapper {
    @Mapping(target = "requester", source = "request.requester.id")
    @Mapping(target = "event", source = "request.event.id")
    ParticipationRequestDto fromRequestToDto(ParticipationRequest request);
}
