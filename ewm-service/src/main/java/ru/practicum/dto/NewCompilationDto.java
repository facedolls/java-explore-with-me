package ru.practicum.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCompilationDto {
    @NotBlank
    String title;
    Boolean pinned;
    List<Long> events;
}
