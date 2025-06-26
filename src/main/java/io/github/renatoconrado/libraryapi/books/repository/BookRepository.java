package io.github.renatoconrado.libraryapi.books.repository;

import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public @Repository interface BookRepository
    extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    @Query("SELECT book FROM Book book ORDER BY book.title, book.price")
    List<Book> listOrderedByTitleAndPrice();

    /**
     * <pre>
     * SELECT author.* FROM book
     * JOIN author ON author.id = book.id_author
     * </pre>
     */
    @Query("SELECT author FROM Book book JOIN book.author author")
    List<Author> listAuthorsOfBook();

    @Transactional
    @Modifying
    @Query("DELETE FROM Book WHERE genres = ?1")
    void deleteByGenre(Genre genre);

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
