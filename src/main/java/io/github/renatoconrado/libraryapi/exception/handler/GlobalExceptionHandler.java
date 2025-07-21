package io.github.renatoconrado.libraryapi.exception.handler;

import io.github.renatoconrado.libraryapi.exception.custom.DuplicatedRecordException;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldException;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldsException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import io.github.renatoconrado.libraryapi.exception.error.ErrorResponse;
import io.github.renatoconrado.libraryapi.exception.error.FieldAndError;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static io.github.renatoconrado.libraryapi.common.constants.Validation.VALIDATION_ERROR;

public @RestControllerAdvice class GlobalExceptionHandler {

/*    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse unhandledException(RuntimeException exception) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                 "Unexpected error: " + exception.getMessage(),
                                 List.of());
    }*/

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception
    ) {
        List<FieldAndError> fieldAndErrors = exception.getFieldErrors()
            .stream()
            .map(fe -> new FieldAndError(fe.getField(), fe.getDefaultMessage()))
            .toList();
        return new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY,
            VALIDATION_ERROR,
            fieldAndErrors
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(
        IllegalArgumentException exception
    ) {
        return ErrorResponse.badRequest(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicatedRecordException.class)
    public ErrorResponse handleDuplicatedRecord(
        DuplicatedRecordException exception
    ) {
        return ErrorResponse.conflict(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidFieldException.class)
    public ErrorResponse handleInvalidFieldException(
        InvalidFieldException exception
    ) {
        return new ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY,
            VALIDATION_ERROR,
            List.of(new FieldAndError(
                exception.getField(),
                exception.getMessage()
            ))
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProcedureNotAllowedException.class)
    public ErrorResponse handleProcedureNotAllowed(
        ProcedureNotAllowedException exception
    ) {
        return ErrorResponse.badRequest(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFieldsException.class)
    public ErrorResponse handleInvalidFields(InvalidFieldsException exception) {
        return ErrorResponse.badRequest(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ErrorResponse handleAuthorizationDeniedException(
        AuthorizationDeniedException exception
    ) {
        return new ErrorResponse(
            HttpStatus.FORBIDDEN,
            "You don't haver access to this resource: " + exception.getAuthorizationResult(),
            List.of()
        );
    }

}
