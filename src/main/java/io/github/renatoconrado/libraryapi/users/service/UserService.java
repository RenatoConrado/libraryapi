package io.github.renatoconrado.libraryapi.users.service;

import io.github.renatoconrado.libraryapi.users.model.Users;
import io.github.renatoconrado.libraryapi.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public @Service class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void save(Users user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.repository.save(user);
    }

    public Users getByUsername(String username) {
        return this.repository.findByLogin(username);
    }

    public Page<Users> query(String username, Integer page, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        if (username != null && !username.isBlank()) {
            return this.repository.findByLoginContainingIgnoreCase(
                username,
                pageRequest
            );
        }
        return this.repository.findAll(pageRequest);
    }
}
