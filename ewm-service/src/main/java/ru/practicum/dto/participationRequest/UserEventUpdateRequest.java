package ru.practicum.dto.participationRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.LocationDto;
import ru.practicum.model.event.UserStateAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEventUpdateRequest {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Size(min = 20, max = 2000)
    String annotation;
    @Size(min = 3, max = 120)
    String title;
    @Size(min = 20, max = 7000)
    String description;
    @Min(0)
    Integer participantLimit;
    Long category;
    LocationDto location;
    @Future
    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;
    Boolean paid;
    Boolean requestModeration;
    UserStateAction stateAction;
}
