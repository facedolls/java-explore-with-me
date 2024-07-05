package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CommentDto {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    Long id;
    UserShortDto author;
    String text;
    @JsonFormat(pattern = DATETIME_FORMAT)
    LocalDateTime createdDate;
}
