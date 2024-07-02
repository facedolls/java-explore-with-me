package ru.practicum.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @NotBlank(message = "Name field is required")
    @Size(min = 2, max = 250)
    String name;
    @NotBlank(message = "Email field is required")
    @Email(regexp = "^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+.[a-zA-Z]{2,})$", message = "Wrong email")
    @Size(min = 6, max = 254)
    String email;
}
