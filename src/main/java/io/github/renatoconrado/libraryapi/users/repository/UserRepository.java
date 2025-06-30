package io.github.renatoconrado.libraryapi.users.repository;

import io.github.renatoconrado.libraryapi.users.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public @Repository interface UserRepository extends JpaRepository<Users, UUID> {

    Users findByLogin(String login);

    Page<Users> findByLoginContainingIgnoreCase(String login, Pageable pageable);
}
