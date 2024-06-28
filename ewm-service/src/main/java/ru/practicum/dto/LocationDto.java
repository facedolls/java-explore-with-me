package ru.practicum.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationDto {
    @NotNull
    Float latitude;
    @NotNull
    Float longitude;
}
