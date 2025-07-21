package io.github.renatoconrado.libraryapi.books.controller.mapper;

import io.github.renatoconrado.libraryapi.authors.controller.mapper.AuthorMapper;
import io.github.renatoconrado.libraryapi.authors.repository.AuthorRepository;
import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.BookDetailsDTO;
import io.github.renatoconrado.libraryapi.books.model.BookRegisterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class BookMapper {
    protected AuthorRepository authorRepository;

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(
        target = "author",
        expression =
            """
            java(authorRepository.findById(dto.idAuthor())
            .orElseThrow(() -> new io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException("The book must have registered author"))
            )"""
    )
    public abstract Book toEntity(BookRegisterDTO dto);

    public abstract BookDetailsDTO toDetailsDTO(Book book);
}
