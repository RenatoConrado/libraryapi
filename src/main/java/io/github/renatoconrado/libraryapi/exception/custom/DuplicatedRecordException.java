package io.github.renatoconrado.libraryapi.exception.custom;

public class DuplicatedRecordException extends RuntimeException {
    public DuplicatedRecordException(String message) {
        super(message);
    }
}
