package ru.practicum.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}

