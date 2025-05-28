package io.github.renatoconrado.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Table(catalog = "library", schema = "public")
public @Entity class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private Genre genres;

    @Column(precision = 18, scale = 4)
    private BigDecimal price;

    /**
     * {@code fetch.LAZY} vai trazer apenas o livro e não o autor.
     * Use {@code @Transaction}
     */
    @ManyToOne(/*cascade = CascadeType.ALL,*/ fetch = FetchType.LAZY)
    @JoinColumn(name = "id_author", nullable = false)
    @EqualsAndHashCode.Exclude
    private Author author;

    public Book() {}

    public Book(
            String isbn,
            String title, LocalDate releaseDate, Genre genres, BigDecimal price, Author author
    ) {
        this.isbn = isbn;
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.price = price;
        this.author = author;
    }

}
