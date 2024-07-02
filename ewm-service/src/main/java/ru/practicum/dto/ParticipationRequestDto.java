package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.ParticipationState;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    Long id;
    Long requester;
    Long event;
    ParticipationState status;

    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime created;
}
