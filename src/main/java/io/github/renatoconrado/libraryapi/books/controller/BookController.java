package io.github.renatoconrado.libraryapi.books.controller;

import io.github.renatoconrado.libraryapi.books.controller.mapper.BookMapper;
import io.github.renatoconrado.libraryapi.books.model.Book;
import io.github.renatoconrado.libraryapi.books.model.BookDetailsDTO;
import io.github.renatoconrado.libraryapi.books.model.BookRegisterDTO;
import io.github.renatoconrado.libraryapi.books.model.Genre;
import io.github.renatoconrado.libraryapi.books.service.BookService;
import io.github.renatoconrado.libraryapi.common.GenericController;
import io.github.renatoconrado.libraryapi.exception.custom.DuplicatedRecordException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("books")
public @RestController class BookController implements GenericController {
    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    public ResponseEntity<Object> create(
        @RequestBody @Valid BookRegisterDTO dto
    ) throws DuplicatedRecordException, ProcedureNotAllowedException {
        Book book = mapper.toEntity(dto);
        service.save(book);
        return ResponseEntity.created(generateHeaderURI(book.getId()))
            .body(Map.of("id", book.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<BookDetailsDTO>> query(
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) String title,
        @RequestParam(required = false, value = "author-name") String author,
        @RequestParam(required = false) Genre genres,
        @RequestParam(required = false, value = "release-year") Integer releaseYear,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "15", value = "page-size") Integer pageSize
    ) {
        Page<BookDetailsDTO> books = service
            .query(isbn, title, author, genres, releaseYear, page, pageSize)
            .map(mapper::toDetailsDTO);
        return books.isEmpty()
               ? ResponseEntity.notFound().build()
               : ResponseEntity.ok(books);
    }

    @GetMapping("{id}")
    public ResponseEntity<BookDetailsDTO> getById(@PathVariable UUID id) {
        return service.getById(id)
            .map(mapper::toDetailsDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> put(
        @PathVariable UUID id,
        @RequestBody @Valid BookRegisterDTO dto
    ) {
        return this.service.update(id, mapper.toEntity(dto))
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        return this.service.delete(id)
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }
}
