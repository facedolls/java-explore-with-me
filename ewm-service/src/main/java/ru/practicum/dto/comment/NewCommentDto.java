package ru.practicum.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewCommentDto {
    @NotBlank
    @Size(min = 5, max = 5555)
    private String text;
}
