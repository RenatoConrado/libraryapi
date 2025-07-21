package io.github.renatoconrado.libraryapi.repository;

import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.authors.repository.AuthorRepository;
import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.Genre;
import io.github.renatoconrado.libraryapi.books.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * <h2> Incrivelmente esses testes funcionam </h2>
 */
@SpringBootTest
class BookRepositoryTest {
    private static final UUID AUTHOR_ID = UUID.fromString(
        "03874eac-3be2-4814-91b8-e4baaf8321eb");
    private static final UUID BOOK_ID = UUID.fromString(
        "add9ec48-9b41-4e2e-9e9e-120d3459df02");
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    BookRepositoryTest(
        BookRepository bookRepository,
        AuthorRepository authorRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Test
    void SaveCascadeTest() {
        Author author = new Author(
            "Jane doe",
            LocalDate.of(1967, 9, 12),
            "United States"
        );
        authorRepository.save(author);

        Book book = new Book(
            "50827-14304",
            "The Greatest Book",
            LocalDate.of(1993, 4, 17),
            Genre.FANTASY,
            BigDecimal.valueOf(150),
            author
        );
        bookRepository.save(book);
        System.out.println(book.getId());
    }

    @Transactional
    @Test
    void findBook() {
        Book book = bookRepository.findById(BOOK_ID).orElseThrow();
        System.out.println("Book: " + book.getId());
        System.out.println("Author: " + book.getAuthor().getId());
    }

    @Test
    void delete() {
        bookRepository.deleteById(BOOK_ID);
    }

    @Test
    void modifyAuthorOfBook() {
        Author author = authorRepository.findById(AUTHOR_ID).orElseThrow();
        Book book = bookRepository.findById(BOOK_ID).orElseThrow();
        book.setAuthor(author);
        bookRepository.save(book);
    }

    @Test
    void UpdateAuthorOfBook() {
        Book book = bookRepository.findById(BOOK_ID).orElseThrow();
        Author author = authorRepository.findById(AUTHOR_ID).orElseThrow();
        book.setAuthor(author);
        bookRepository.save(book);
    }

    @Test
    @Transactional
    void ListByTitleAndPrice() {
        System.out.println(bookRepository.listOrderedByTitleAndPrice());
    }

    @Test
    void listAuthorsOfBook() {
        List<Author> result = bookRepository.listAuthorsOfBook();
        result.forEach(System.out::println);
    }

    @Test
    void deleteByGenre() {
        bookRepository.deleteByGenre(Genre.FANTASY);
    }
}