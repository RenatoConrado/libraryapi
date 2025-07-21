package io.github.renatoconrado.libraryapi.books.repository;

import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.Genre;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldException;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.Locale;

import static java.lang.String.format;

public class BookSpecification {

    private static String upperPattern(String str) {
        return "%" + str.strip().toUpperCase(Locale.ROOT) + "%";
    }

    public static @Nullable Specification<Book> isbnEquals(final String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get("isbn"), isbn.strip());
    }

    public static @Nullable Specification<Book> titleLike(final String title) {
        if (title == null || title.isBlank()) {
            return null;
        }
        return (root, query, builder) -> builder.like(
            builder.upper(root.get("title")),
            upperPattern(title)
        );
    }

    public static @Nullable Specification<Book> genreEqual(final Genre genres) {
        if (genres == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get("genres"), genres);
    }

    public static @Nullable Specification<Book> releaseYearEqual(final Integer releaseYear) {
        if (releaseYear == null) {
            return null;
        }
        if ((releaseYear < 1000) || (releaseYear > 2100)) {
            throw new InvalidFieldException(
                "Invalid year: " + releaseYear,
                "release-year"
            );
        }

        return (root, query, builder) -> {
            Expression<String> queryExpression = builder.function(
                "to_char",
                String.class,
                root.get("releaseDate"),
                builder.literal("YYYY")
            );
            String stringToCompare = format(Locale.ROOT, "%04d", releaseYear);

            return builder.equal(queryExpression, stringToCompare);
        };
    }

    public static @Nullable Specification<Book> authorLike(final String author) {
        if (author == null || author.isBlank()) {
            return null;
        }

        return (root, query, builder) -> {
            Join<Object, Object> joinAuthor = root.join("author", JoinType.INNER);
            Expression<String> upperExpression = builder.upper(joinAuthor.get("name"));

            return builder.like(upperExpression, upperPattern(author));
        };

/*        return (root, query, cb) -> cb.like(
            cb.upper(root.get("author").get("name"))
        , "%" + author + "%");*/
    }
}
