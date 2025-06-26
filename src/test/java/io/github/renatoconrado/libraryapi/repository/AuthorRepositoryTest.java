package io.github.renatoconrado.libraryapi.repository;

import io.github.renatoconrado.libraryapi.authors.repository.AuthorRepository;
import io.github.renatoconrado.libraryapi.books.repository.BookRepository;
import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * <h2> Incrivelmente esses testes funcionam </h2>
 */
@SpringBootTest class AuthorRepositoryTest
{
    private static final UUID AUTHOR_ID = UUID.fromString("03874eac-3be2-4814-91b8-e4baaf8321eb");
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired AuthorRepositoryTest(AuthorRepository authorRepository, BookRepository bookRepository)
    {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Test void getAllTest()
    { authorRepository.findAll(); }

    @Test void saveTest()
    {
        Author author = new Author("John Doe", LocalDate.of(1995, 10, 15), "United States");
        authorRepository.save(author);
    }

    @Test void updateTest()
    {
        Author author = authorRepository.findById(AUTHOR_ID).orElseThrow();
        author.setBirthdate(LocalDate.of(2005, 7, 3));
        authorRepository.save(author);
    }

    @Test void ListTest()
    {
        authorRepository.findAll().forEach(author -> System.out.println(author.getName()));
    }

    @Test void countTest()
    {
        System.out.println("Count Authors: " + authorRepository.count());
    }

    @Test void deleteTest()
    {
        authorRepository.deleteById(AUTHOR_ID);
    }

    @Test void deleteAllTest()
    {
        authorRepository.deleteAll();
    }

    @Test void SaveAuthorAndBooks()
    {
        Author author = new Author("William Doe", LocalDate.of(1995, 10, 15), "England");
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
        authorRepository.save(author);
        bookRepository.saveAll(author.getBooks());
    }
}
