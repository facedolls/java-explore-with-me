package ru.practicum;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StatsMapper {
    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);

    EndpointHitDto toEndpointHitDto(EndpointHit endpointHit);

    ViewStatsDto toViewStats(ViewStats viewStats);

    List<ViewStatsDto> toViewStatsDtoList(List<ViewStats> viewStats);
}
