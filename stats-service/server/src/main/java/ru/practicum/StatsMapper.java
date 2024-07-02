package ru.practicum;

import lombok.experimental.UtilityClass;
import ru.practicum.model.App;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class StatsMapper {
    public static EndpointHitDto fromHitToDto(EndpointHit hit) {
        return EndpointHitDto.builder()
                .app(hit.getApp().getName())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .time(hit.getTimestamp())
                .build();
    }

    public static EndpointHit fromDtoToHit(EndpointHitDto dto, App app) {
        EndpointHit hit = new EndpointHit();
        hit.setApp(app);
        hit.setUri(dto.getUri());
        hit.setIp(dto.getIp());
        hit.setTimestamp(dto.getTime());
        return hit;
    }

    public static ViewStatsDto fromViewStatToDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp().getName())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

    public static List<ViewStatsDto> fromListViewStatToDto(List<ViewStats> viewStatsList) {
        return viewStatsList.stream()
                .map(StatsMapper::fromViewStatToDto)
                .collect(Collectors.toList());
    }
}
