package ru.practicum.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCompilationDto {
    @NotBlank
    @Size(min = 1, max = 50)
    String title;
    Boolean pinned;
    List<Long> events;
}
