package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    Long id;
    String annotation;
    String title;

    CategoryDto category;
    UserShortDto initiator;

    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;

    Boolean paid;
    Long confirmedRequests;
    Long views;
}

