package io.github.renatoconrado.libraryapi.authors.service;

import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.authors.model.AuthorDTO;
import io.github.renatoconrado.libraryapi.authors.repository.AuthorRepository;
import io.github.renatoconrado.libraryapi.exception.custom.DuplicatedRecordException;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldsException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public @Service class AuthorService {

    private final AuthorRepository repository;
    private final AuthorValidator validator;

    /**
     * @deprecated use {@code  queryByExample()} instead
     */
    public List<Author> query(String name, String citizenship) {
        return repository.findAllByNameContainingIgnoreCaseAndCitizenshipContainingIgnoreCase(
            name,
            citizenship);
    }

    public List<Author> queryByExample(String name, String citizenship) {
        var author = new Author();
        author.setName(name);
        author.setCitizenship(citizenship);
        var matcher = ExampleMatcher
            .matching()
            .withIgnorePaths("id", "created_at", "updated_at")
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(StringMatcher.CONTAINING);
        return repository.findAll(Example.of(author, matcher));
    }

    /**
     * @throws DuplicatedRecordException if Author is Duplicated
     */
    public void create(@Valid Author author) throws DuplicatedRecordException {
        validator.validate(AuthorDTO.of(author));
        repository.save(author);
    }

    public Optional<Author> getById(UUID uuid) {
        return repository.findById(uuid);
    }

    /**
     * @return {@code true} if updated, {@code false} if author not found or DTO is invalid
     * @throws InvalidFieldsException if all fields are empty
     */
    public boolean update(UUID id, AuthorDTO dto) throws InvalidFieldsException {
        validator.emptyFields(dto);
        return getById(id)
            .map(author -> {
                author.setName(dto.name());
                author.setBirthdate(dto.birthdate());
                author.setCitizenship(dto.citizenship());
                repository.save(author);
                return true;
            })
            .orElse(false);
    }

    /**
     * @return {@code true} if deleted, {@code false} if author not found
     * @throws ProcedureNotAllowedException if author has books
     */
    public boolean delete(UUID uuid) throws ProcedureNotAllowedException {
        return repository
            .findById(uuid)
            .map(author -> {
                validator.hasBook(author);
                repository.delete(author);
                return true;
            })
            .orElse(false);
    }

}
