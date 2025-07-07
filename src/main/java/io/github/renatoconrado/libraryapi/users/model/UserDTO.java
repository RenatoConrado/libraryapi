package io.github.renatoconrado.libraryapi.users.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDTO(
    @NotBlank(message = "Field Required")
    String login,
    @Email(message = "Field Invalid")
    @NotBlank(message = "Field Required")
    String email,
    @NotBlank(message = "Field Required")
    String password,
    @NotNull(message = "Field Required")
    List<String> roles
) {
}
