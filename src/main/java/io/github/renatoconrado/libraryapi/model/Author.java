package io.github.renatoconrado.libraryapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Table(catalog /* Database */ = "library", schema = "public")
public @Entity class Author {

    @GeneratedValue(strategy = GenerationType.UUID)
    private @Id UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false, length = 50)
    private String citizenship;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    public Author() {}
    public Author(String name, LocalDate birthdate, String citizenship) {
        this.name = name;
        this.birthdate = birthdate;
        this.citizenship = citizenship;
    }
}
