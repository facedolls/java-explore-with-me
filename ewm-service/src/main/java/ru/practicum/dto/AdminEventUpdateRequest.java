package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.event.AdminStateAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminEventUpdateRequest {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Size(min = 20, max = 2000)
    String annotation;

    @Size(min = 3, max = 120)
    String title;

    @Size(min = 20, max = 7000)
    String description;

    Integer participantLimit;
    Long category;
    LocationDto location;

    @Future
    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;

    Boolean paid;
    Boolean requestModeration;
    AdminStateAction stateAction;
}
