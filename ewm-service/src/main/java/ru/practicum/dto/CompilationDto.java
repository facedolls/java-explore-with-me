package ru.practicum.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompilationDto {
    Long id;
    Boolean pinned;
    String title;
    List<EventShortDto> events;
}
