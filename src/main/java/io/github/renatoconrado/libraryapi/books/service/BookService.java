package io.github.renatoconrado.libraryapi.books.service;

import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.Genre;
import io.github.renatoconrado.libraryapi.books.repository.BookRepository;
import io.github.renatoconrado.libraryapi.exception.custom.DuplicatedRecordException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import io.github.renatoconrado.libraryapi.users.service.SecurityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.github.renatoconrado.libraryapi.books.repository.BookSpecification.*;
import static io.github.renatoconrado.libraryapi.common.constants.Pageable.normalizePage;
import static io.github.renatoconrado.libraryapi.common.constants.Pageable.normalizePageSize;
import static java.lang.System.err;

@RequiredArgsConstructor
public @Service class BookService {
    private final BookRepository repository;
    private final BookValidator validator;
    private final SecurityUserService securityUserService;

    /**
     * @throws DuplicatedRecordException    if ISBN of the book already exists
     * @throws ProcedureNotAllowedException if the book don't have a registered author
     */
    public void save(Book book) {
        validator.validate(book);

        securityUserService.getLoggedUser().ifPresentOrElse(book::setUser, err::println);

        repository.save(book);
    }

    public Page<Book> query(
        String isbn,
        String title,
        String author,
        Genre genres,
        Integer releaseYear,
        Integer page,
        Integer pageSize
    ) {
        Specification<Book> specs = (root, query, cb) -> null;

        specs = specs.and(isbnEquals(isbn))
            .and(titleLike(title))
            .and(genreEqual(genres))
            .and(releaseYearEqual(releaseYear))
            .and(authorLike(author));

        Pageable pageRequest = PageRequest.of(
            normalizePage(page),
            normalizePageSize(pageSize)
        );

        return repository.findAll(specs, pageRequest);
    }

    public Optional<Book> getById(UUID id) {
        return repository.findById(id);
    }

    public boolean update(UUID id, Book newBook) {
        return repository.findById(id).map(book -> {
            book.setIsbn(newBook.getIsbn());
            book.setTitle(newBook.getTitle());
            book.setReleaseDate(newBook.getReleaseDate());
            book.setGenres(newBook.getGenres());
            book.setAuthor(newBook.getAuthor());

            repository.save(book);
            return true;
        }).orElse(false);
    }

    public boolean delete(UUID id) {
        return repository.findById(id).map(book -> {
            repository.delete(book);
            return true;
        }).orElse(false);
    }

}
