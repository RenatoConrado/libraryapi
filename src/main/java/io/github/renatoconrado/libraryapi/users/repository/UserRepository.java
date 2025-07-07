package io.github.renatoconrado.libraryapi.users.repository;

import io.github.renatoconrado.libraryapi.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public @Repository interface UserRepository
    extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);
}
