package io.github.renatoconrado.libraryapi.exception.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
    HttpStatus status,
    String message,
    List<FieldAndError> errors
) {
    public static ErrorResponse badRequest(String message) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, message, List.of());
    }

    public static ErrorResponse conflict(String message) {
        return new ErrorResponse(HttpStatus.CONFLICT, message, List.of());
    }
}
