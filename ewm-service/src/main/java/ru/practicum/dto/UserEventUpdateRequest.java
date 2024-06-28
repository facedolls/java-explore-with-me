package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.UserStateAction;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEventUpdateRequest {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String annotation;
    String title;
    String description;
    Integer participantLimit;
    Long category;
    LocationDto location;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;

    Boolean paid;
    Boolean requestModeration;
    UserStateAction stateAction;
}
