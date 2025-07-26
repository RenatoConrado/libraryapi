package io.github.renatoconrado.libraryapi.authors.model;

import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.users.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(catalog = "library", schema = "public")
public @Entity class Author {

    @GeneratedValue(strategy = GenerationType.UUID)
    private @Id UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false, length = 50)
    private String citizenship;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    public Author(String name, LocalDate birthdate, String citizenship) {
        this.name = name;
        this.birthdate = birthdate;
        this.citizenship = citizenship;
    }

    public Author(AuthorDTO dto) {
        this(dto.name(), dto.birthdate(), dto.citizenship());
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
                                      ? ((HibernateProxy) this)
                                          .getHibernateLazyInitializer()
                                          .getPersistentClass()
                                      : getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        Author author = (Author) o;
        return id != null && Objects.equals(id, author.id);
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
