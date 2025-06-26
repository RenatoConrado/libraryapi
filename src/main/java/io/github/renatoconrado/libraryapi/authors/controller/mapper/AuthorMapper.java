package io.github.renatoconrado.libraryapi.authors.controller.mapper;

import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.authors.model.AuthorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toEntity(AuthorDTO authorDTO);
    AuthorDTO toDto(Author author);
}
