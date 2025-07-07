package io.github.renatoconrado.libraryapi.users.repository;

import io.github.renatoconrado.libraryapi.users.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> UsernameLike(String username) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get("login")),
            "%" + username.toLowerCase() + "%"
        );
    }

    public static Specification<User> EmailLike(String email) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get("email")),
            "%" + email + "%"
        );
    }
}
