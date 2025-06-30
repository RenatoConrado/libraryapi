package io.github.renatoconrado.libraryapi.authors.model;

import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.users.model.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Data
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
    private Users user;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    public Author() {}

    public Author(String name, LocalDate birthdate, String citizenship) {
        this.name = name;
        this.birthdate = birthdate;
        this.citizenship = citizenship;
    }

    public Author(AuthorDTO dto) {
        this(dto.name(), dto.birthdate(), dto.citizenship());
    }
}
