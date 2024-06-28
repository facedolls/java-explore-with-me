package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.EventState;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
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
