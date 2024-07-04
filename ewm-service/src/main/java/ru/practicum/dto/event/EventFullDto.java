package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.event.EventState;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    Long id;

    String annotation;
    String title;
    String description;
    Integer participantLimit;

    CategoryDto category;
    UserShortDto initiator;
    Location location;

    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime createdOn;

    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime publishedOn;

    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;

    EventState state;
    Boolean paid;

    Boolean requestModeration;
    Long confirmedRequests;
    Long views;
}

