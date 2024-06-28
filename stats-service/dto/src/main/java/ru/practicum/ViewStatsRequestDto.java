package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ViewStatsRequestDto {
    LocalDateTime start;
    LocalDateTime end;
    List<String> uris;
    boolean unique;
}
