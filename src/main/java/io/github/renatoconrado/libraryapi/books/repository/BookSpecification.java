package io.github.renatoconrado.libraryapi.books.repository;

import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.Genre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> isbnEquals(final String isbn) {
        return (root, query, cb) -> cb.equal(
            root.get("isbn"),
            isbn);
    }

    public static Specification<Book> titleLike(final String title) {
        return (root, query, cb) -> cb.like(
            cb.upper(root.get("title")),
            "%" + title.toUpperCase() + "%"
        );
    }

    public static Specification<Book> genreEqual(final Genre genres) {
        return (root, query, cb) -> cb.equal(root.get("genres"), genres);
    }

    public static Specification<Book> releaseYearEqual(final Integer releaseYear) {
        return (root, query, cb) -> cb.equal(
            cb.function("to_char", String.class, root.get("releaseDate"), cb.literal("YYYY")),
            releaseYear.toString()
        );
    }

    public static Specification<Book> authorLike(final String author) {
        return (root, query, cb) -> {
            Join<Object, Object> joinAuthor = root.join("author", JoinType.INNER);
            return cb.like(
                cb.upper(joinAuthor.get("name")),
                "%" + author.toUpperCase() + "%"
            );
        };

/*        return (root, query, cb) -> cb.like(
            cb.upper(root.get("author").get("name"))
        , "%" + author + "%");*/
    }
}
