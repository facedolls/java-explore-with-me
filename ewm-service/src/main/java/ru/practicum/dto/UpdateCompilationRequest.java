package ru.practicum.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateCompilationRequest {
    @Length(min = 1, max = 50)
    String title;
    Boolean pinned;
    List<Long> events;
}
