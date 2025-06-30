package io.github.renatoconrado.libraryapi.authors.controller;

import io.github.renatoconrado.libraryapi.authors.controller.mapper.AuthorMapper;
import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.authors.model.AuthorDTO;
import io.github.renatoconrado.libraryapi.authors.service.AuthorService;
import io.github.renatoconrado.libraryapi.common.GenericController;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldsException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import io.github.renatoconrado.libraryapi.users.model.Users;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("authors")
public @RestController class AuthorController implements GenericController {
    private final AuthorService authorService;
    private final UserService userService;
    private final AuthorMapper mapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> query(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String citizenship
    ) {
        List<AuthorDTO> authorDTOs = authorService.queryByExample(name, citizenship)
            .stream()
            .map(AuthorDTO::of)
            .toList();
        if (authorDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorDTOs);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid AuthorDTO dto) {
        Author author = mapper.toEntity(dto);
        authorService.create(author);
        return ResponseEntity.created(generateHeaderURI(author.getId())).build();
    }

    @Deprecated
    public ResponseEntity<Object> oldCreate(
        @RequestBody @Valid AuthorDTO dto,
        Authentication authentication
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users user = userService.getByUsername(userDetails.getUsername());

        Author author = mapper.toEntity(dto);
        author.setUser(user);

        authorService.create(author);
        return ResponseEntity.created(generateHeaderURI(author.getId())).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable UUID id) {
        return authorService.getById(id)
            .map(mapper::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(
        @PathVariable UUID id,
        @RequestBody @Valid AuthorDTO authorDTO
    ) throws InvalidFieldsException {
        return authorService.update(id, authorDTO)
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id)
        throws ProcedureNotAllowedException {
        return authorService.delete(id)
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }
}
