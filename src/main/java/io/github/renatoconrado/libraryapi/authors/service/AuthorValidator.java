package io.github.renatoconrado.libraryapi.authors.service;

import io.github.renatoconrado.libraryapi.authors.model.AuthorDTO;
import io.github.renatoconrado.libraryapi.exception.custom.DuplicatedRecordException;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldsException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.authors.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public @Component class AuthorValidator {
    private final AuthorRepository authorRepository;

    public void validate(AuthorDTO dto) throws DuplicatedRecordException {
        if (authorRepository.existsByNameAndBirthdateAndCitizenship(dto.name(), dto.birthdate(), dto.citizenship())) {
            throw new DuplicatedRecordException("Author already exists");
        }
    }

    public void emptyFields(AuthorDTO dto) throws InvalidFieldsException {
        if ((dto.name() == null || dto.name().isBlank())
                && (dto.birthdate() == null)
                && (dto.citizenship() == null || dto.citizenship().isBlank())) {
            throw new InvalidFieldsException("Fields cannot be empty");
        }
    }

    public void hasBook(Author author) throws ProcedureNotAllowedException {
        if (!author.getBooks().isEmpty()) {
            throw new ProcedureNotAllowedException("The exclusion of an author with books is not allowed");
        }
    }
}
