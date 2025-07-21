package io.github.renatoconrado.libraryapi.books.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookRegisterDTO(
    @ISBN
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
    BigDecimal price,

    @NotNull(message = "Required Field")
    UUID idAuthor
) {
}
