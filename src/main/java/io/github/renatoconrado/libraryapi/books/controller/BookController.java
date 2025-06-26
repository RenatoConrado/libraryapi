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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("books")
public @RestController class BookController implements GenericController {
    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookRegisterDTO dto)
        throws DuplicatedRecordException, ProcedureNotAllowedException {
        Book book = mapper.toEntity(dto);
        service.save(book);
        return ResponseEntity.created(generateHeaderURI(book.getId()))
                             .body(Map.of("id", book.getId()));
    }

    @GetMapping
    public ResponseEntity<List<BookDetailsDTO>> query(
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) Genre genres,
        @RequestParam(required = false) Integer releaseYear
    ) {
        List<BookDetailsDTO> books = service
            .query(isbn, title, author, genres, releaseYear)
            .stream()
            .map(mapper::toDetailsDTO)
            .toList();
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
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
        return service.update(id, mapper.toEntity(dto))
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        return service.delete(id)
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }
}
