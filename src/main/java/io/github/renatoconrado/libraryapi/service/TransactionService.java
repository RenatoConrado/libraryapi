package io.github.renatoconrado.libraryapi.service;

import io.github.renatoconrado.libraryapi.model.Author;
import io.github.renatoconrado.libraryapi.model.Book;
import io.github.renatoconrado.libraryapi.model.Genre;
import io.github.renatoconrado.libraryapi.repository.AuthorRepository;
import io.github.renatoconrado.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@RequiredArgsConstructor
public @Service class TransactionService
{
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional public void main()
    {
        Author author = new Author("William Doe", LocalDate.of(1995, 10, 15), "England");
        authorRepository.save(author);

        Book book1 = new Book("02585-98367",
                              "The Haunted House",
                              LocalDate.of(1940, 7, 12),
                              Genre.MYSTERY,
                              BigDecimal.valueOf(40),
                              author);
        Book book2 = new Book("98076-28975",
                              "The Worst Book Ever",
                              LocalDate.of(2018, 8, 27),
                              Genre.BIBLIOGRAPHY,
                              BigDecimal.valueOf(180),
                              author);
        Set<Book> bookSet = Set.of(book1, book2);
        author.setBooks(bookSet);
        bookRepository.saveAll(author.getBooks());
    }
}
