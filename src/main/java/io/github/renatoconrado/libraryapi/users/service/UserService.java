package io.github.renatoconrado.libraryapi.users.service;

import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.users.repository.UserRepository;
import io.github.renatoconrado.libraryapi.users.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
public @Service class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    public Optional<User> getByUsername(String username) {
        return repository.findByLogin(username);
    }

    public Optional<User> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Page<User> query(
        String username,
        String email,
        Integer page,
        Integer pageSize
    ) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        Specification<User> spec = (root, query, cb) -> null;

        if (username != null && !username.isBlank()) {
            spec = spec.and(UserSpecification.UsernameLike(username));
        }
        if (email != null) {
            spec = spec.and(UserSpecification.EmailLike(email));
        }

        return repository.findAll(spec, pageRequest);
    }
}
