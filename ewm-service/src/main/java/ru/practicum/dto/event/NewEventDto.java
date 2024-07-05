package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @NotNull
    @Min(1L)
    Long category;
    @NotNull
    LocationDto location;
    @NotNull
    @Future(message = "Event should be in future")
    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;
    Boolean paid;
    @Min(0)
    Integer participantLimit;
    Boolean requestModeration;
}
