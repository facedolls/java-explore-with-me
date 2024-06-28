package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.AdminStateAction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminEventUpdateRequest {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Length(min = 20, max = 2000)
    String annotation;
    @Length(min = 3, max = 120)
    String title;
    @Length(min = 20, max = 7000)
    String description;
    Integer participantLimit;
    Long category;
    LocationDto location;
    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime eventDate;
    Boolean paid;
    Boolean requestModeration;
    AdminStateAction stateAction;
}
