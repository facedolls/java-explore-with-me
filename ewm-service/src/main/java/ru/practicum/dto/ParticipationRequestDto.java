package ru.practicum.dto;

import lombok.*;
import ru.practicum.model.ParticipationState;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParticipationRequestDto {
    Long id;
    Long requester;
    Long event;
    ParticipationState status;
    LocalDateTime created;
}
