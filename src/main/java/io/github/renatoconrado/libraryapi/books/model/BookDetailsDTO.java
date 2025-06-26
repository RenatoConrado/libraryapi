package io.github.renatoconrado.libraryapi.books.model;

import io.github.renatoconrado.libraryapi.authors.model.AuthorDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookDetailsDTO(
    UUID id,

    @NotBlank(message = "Required Field")
    String isbn,

    @NotBlank(message = "Required Field")
    String title,

    @Past(message = "Future Books not allowed")
    @NotNull(message = "Required Field")
    LocalDate releaseDate,

    @NotNull(message = "Required Field")
    Genre genres,

    @Positive(message = "Value must be positive")
    @NotNull(message = "Required Field")
    BigDecimal price,

    @NotNull(message = "Required Field")
    AuthorDTO author
) {
}
