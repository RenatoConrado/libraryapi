package io.github.renatoconrado.libraryapi.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleClientNotFound(
        ClientNotFoundException exception
    ) {
        Map<String, String> response = new HashMap<>(Map.of(
            "Error", "Client not found.",
            "message", exception.getMessage()
        ));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
