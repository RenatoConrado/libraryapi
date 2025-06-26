package io.github.renatoconrado.libraryapi.authors.repository;

import io.github.renatoconrado.libraryapi.authors.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public @Repository interface AuthorRepository extends JpaRepository<Author, UUID> {
    List<Author> findAllByNameContainingIgnoreCaseAndCitizenshipContainingIgnoreCase(String name, String citizenship);

    boolean existsByNameAndBirthdateAndCitizenship(String name, LocalDate birthdate, String citizenship);
}
