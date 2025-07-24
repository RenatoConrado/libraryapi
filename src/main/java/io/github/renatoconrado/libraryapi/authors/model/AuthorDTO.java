package io.github.renatoconrado.libraryapi.authors.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Author")
public record AuthorDTO(
    UUID id,

    @NotBlank(message = "Required field")
    @Size(
        min = 4,
        max = 100,
        message = "Size outside valid range"
    )
    String name,

    @NotNull(message = "Required field")
    @Past(message = "tá viajando no tempo agora?")
    LocalDate birthdate,

    @NotBlank(message = "Required field")
    @Size(
        min = 4,
        max = 50,
        message = "Size outside valid range"
    )
    String citizenship
) {
    public static AuthorDTO of(Author a) {
        return new AuthorDTO(
            a.getId(),
            a.getName(),
            a.getBirthdate(),
            a.getCitizenship()
        );
    }
}
