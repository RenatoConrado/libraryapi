package io.github.renatoconrado.libraryapi.books.service;

import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.Genre;
import io.github.renatoconrado.libraryapi.books.repository.BookRepository;
import io.github.renatoconrado.libraryapi.exception.custom.DuplicatedRecordException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import io.github.renatoconrado.libraryapi.users.model.Users;
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

@RequiredArgsConstructor
public @Service class BookService {
    private final BookRepository repository;
    private final BookValidator validator;
    private final SecurityUserService securityUserService;

    /**
     * @throws DuplicatedRecordException    if ISBN of the book already exists
     * @throws ProcedureNotAllowedException if the book don't have a registered author
     */
    public void save(Book book) throws DuplicatedRecordException {
        this.validator.validate(book);

        Users user = securityUserService.getLoggedUser();
        book.setUser(user);

        this.repository.save(book);
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
        Specification<Book> specs = (root, query, criteriaBuilder) -> null;
        if (isbn != null) {
            specs = specs.and(isbnEquals(isbn));
        }
        if (title != null) {
            specs = specs.and(titleLike(title));
        }
        if (genres != null) {
            specs = specs.and(genreEqual(genres));
        }
        if (releaseYear != null) {
            specs = specs.and(releaseYearEqual(releaseYear));
        }
        if (author != null) {
            specs = specs.and(authorLike(author));
        }
        Pageable pageRequest = PageRequest.of(page, pageSize);
        return repository.findAll(specs, pageRequest);
    }

    public Optional<Book> getById(UUID id) {
        return this.repository.findById(id);
    }

    public boolean update(UUID id, Book newBook) {
        return this.repository.findById(id).map(book -> {
            book.setIsbn(newBook.getIsbn());
            book.setTitle(newBook.getTitle());
            book.setReleaseDate(newBook.getReleaseDate());
            book.setGenres(newBook.getGenres());
            book.setAuthor(newBook.getAuthor());
            this.save(book);
            return true;
        }).orElse(false);
    }

    public boolean delete(UUID id) {
        return this.repository.findById(id).map(book -> {
            this.repository.delete(book);
            return true;
        }).orElse(false);
    }

}
