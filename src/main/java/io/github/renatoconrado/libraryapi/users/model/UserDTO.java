package io.github.renatoconrado.libraryapi.users.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDTO(
    @NotBlank(message = "Field Required")
    String login,
    @NotBlank(message = "Field Required")
    String password,
    @NotNull(message = "Field Required")
    List<String> roles
) {
}
