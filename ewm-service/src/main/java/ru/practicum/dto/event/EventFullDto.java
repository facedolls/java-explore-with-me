package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.Location;
import ru.practicum.model.event.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class EventFullDto {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = DATETIME_FORMAT)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = DATETIME_FORMAT)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(pattern = DATETIME_FORMAT)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}
