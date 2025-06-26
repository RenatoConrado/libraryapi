package io.github.renatoconrado.libraryapi.exception.custom;

public class ProcedureNotAllowedException extends RuntimeException {
    public ProcedureNotAllowedException(String message) {
        super(message);
    }
}
