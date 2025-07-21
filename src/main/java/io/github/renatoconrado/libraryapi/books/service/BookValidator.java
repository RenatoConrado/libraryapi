package io.github.renatoconrado.libraryapi.books.service;

import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.repository.BookRepository;
import io.github.renatoconrado.libraryapi.exception.custom.DuplicatedRecordException;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public @Component class BookValidator {
    private final BookRepository repository;

    /**
     * @param book
     * @throws DuplicatedRecordException
     * @throws InvalidFieldException
     */
    public void validate(Book book) {
        if (duplicatedISBN(book)) {
            throw new DuplicatedRecordException("ISBN already registered " + book.getIsbn());
        }
        if (bookWithoutPriceAndIsReleasedAfter2020(book)) {
            throw new InvalidFieldException(
                "Pricing is mandatory for books released after 2020",
                "price"
            );
        }
    }

    private boolean bookWithoutPriceAndIsReleasedAfter2020(Book book) {
        return book.getPrice() == null
            && book.getReleaseDate().isAfter(LocalDate.ofYearDay(2019, 365));
    }

    private boolean duplicatedISBN(Book book) {
        Optional<Book> foundBook = repository.findByIsbn(book.getIsbn());
        if (book.getId() == null) {
            return foundBook.isPresent();
        }
        return foundBook.map(Book::getId)
            .map(book.getId()::equals)
            .orElse(false);
    }
}
