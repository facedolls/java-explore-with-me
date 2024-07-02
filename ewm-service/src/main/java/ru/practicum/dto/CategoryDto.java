package ru.practicum.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    Long id;
    @NotBlank(message = "Name field required")
    @Size(min = 1, max = 50)
    String name;
}
