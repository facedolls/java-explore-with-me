package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    HttpStatus status;
    String reason;
    String message;
    private StackTraceElement[] errors;
    @JsonProperty("timestamp")
    @JsonFormat(pattern = DATETIME_FORMAT)
    private LocalDateTime errorTime;
}
