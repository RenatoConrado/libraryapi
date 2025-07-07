package io.github.renatoconrado.libraryapi.users.model;

import java.util.List;

public record UserSafeDTO(
    String username,
    String email,
    List<String> roles
) {
}
