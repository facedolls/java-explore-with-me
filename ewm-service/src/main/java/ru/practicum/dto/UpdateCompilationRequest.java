package ru.practicum.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateCompilationRequest {
    String title;
    Boolean pinned;
    List<Long> events;
}
