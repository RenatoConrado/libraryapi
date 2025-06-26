package io.github.renatoconrado.libraryapi.authors.controller;

import io.github.renatoconrado.libraryapi.authors.controller.mapper.AuthorMapper;
import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.authors.model.AuthorDTO;
import io.github.renatoconrado.libraryapi.authors.service.AuthorService;
import io.github.renatoconrado.libraryapi.common.GenericController;
import io.github.renatoconrado.libraryapi.exception.custom.DuplicatedRecordException;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldsException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("authors")
public @RestController class AuthorController implements GenericController {
    private final AuthorService service;
    private final AuthorMapper mapper;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> query(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String citizenship
    ) {
        List<AuthorDTO> authorDTOs = service
            .queryByExample(name, citizenship)
            .stream()
            .map(AuthorDTO::of)
            .toList();
        if (authorDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorDTOs);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid AuthorDTO dto)
        throws DuplicatedRecordException {
        Author author = mapper.toEntity(dto);
        service.create(author);
        return ResponseEntity.created(generateHeaderURI(author.getId()))
                             .body(Map.of("id", author.getId()));
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable UUID id) {
        return service
            .getById(id)
            .map(mapper::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(
        @PathVariable UUID id,
        @RequestBody @Valid AuthorDTO authorDTO
    ) throws InvalidFieldsException {
        return service.update(id, authorDTO)
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id)
        throws ProcedureNotAllowedException {
        return service.delete(id)
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }
}
