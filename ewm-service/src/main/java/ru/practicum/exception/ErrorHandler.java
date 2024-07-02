package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException exc) {
        log.error("Validation error", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(exc.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException exc) {
        log.error("Request was made incorrectly", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(exc.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleInvalidTimeException(final InvalidTimeException exc) {
        log.error("Date setting error", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("For the requested operation the conditions are not met.")
                .message(exc.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exc) {
        log.error("Object was not found", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(exc.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable exc) {
        log.error("Server error", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason(exc.getClass() + " Internal server error.")
                .message(exc.getMessage())
                .errors(exc.getStackTrace())
                .errorTime(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException exc) {
        log.error("Conditions are not met for requested operation", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .reason("For the requested operation the conditions are not met.")
                .message(exc.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestParameterException(MissingServletRequestParameterException exc) {
        log.error("Request was made incorrectly", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(exc.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException exc) {
        log.error("Integrity violated", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(exc.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
    }
}
