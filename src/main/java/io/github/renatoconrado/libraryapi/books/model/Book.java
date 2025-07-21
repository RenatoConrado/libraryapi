package io.github.renatoconrado.libraryapi.books.model;

import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.users.model.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(
    catalog = "library",
    schema = "public"
)
public @Entity class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
        nullable = false,
        unique = true,
        length = 20
    )
    private String isbn;

    @Column(
        nullable = false,
        length = 150
    )
    private String title;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genres;

    @Column(
        precision = 18,
        scale = 4
    )
    private BigDecimal price;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    /**
     * {@code fetch.LAZY} vai trazer apenas o livro e não o autor.
     * Use {@code @Transaction}
     */
    @ManyToOne(/*cascade = CascadeType.ALL,*/ fetch = FetchType.LAZY)
    @JoinColumn(name = "id_author", nullable = false)
    @EqualsAndHashCode.Exclude @ToString.Exclude
    private Author author;

    public Book(
        String isbn,
        String title,
        LocalDate releaseDate,
        Genre genres,
        BigDecimal price,
        Author author
    ) {
        this.isbn = isbn;
        this.title = title;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.price = price;
        this.author = author;
    }

    public static Book of(@Valid BookRegisterDTO dto) {
        return new Book(
            dto.isbn(),
            dto.title(),
            dto.releaseDate(),
            dto.genres(),
            dto.price(),
            null
        );
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                                   ? ((HibernateProxy) o).getHibernateLazyInitializer()
                                       .getPersistentClass()
                                   : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                                      ? ((HibernateProxy) this).getHibernateLazyInitializer()
                                          .getPersistentClass()
                                      : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        Book book = (Book) o;
        return getId() != null && Objects.equals(getId(), book.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
               ? ((HibernateProxy) this).getHibernateLazyInitializer()
                   .getPersistentClass()
                   .hashCode()
               : getClass().hashCode();
    }
}
