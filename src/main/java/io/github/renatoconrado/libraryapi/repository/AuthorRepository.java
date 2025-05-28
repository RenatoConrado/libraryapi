package io.github.renatoconrado.libraryapi.repository;

import io.github.renatoconrado.libraryapi.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public @Repository interface AuthorRepository extends JpaRepository<Author, UUID> {
}
