package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Length(min = 20, max = 2000)
    String annotation;
    @NotBlank
    String title;
    @NotBlank
    @Length(min = 20, max = 7000)
    String description;
    @NotNull
    Long category;
    @NotNull
    LocationDto location;
    @NotNull
    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
}
