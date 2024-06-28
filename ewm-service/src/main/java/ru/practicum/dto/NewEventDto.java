package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewEventDto {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @NotBlank
    String annotation;
    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotNull
    Long category;
    @NotNull
    LocationDto location;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
}
