package io.github.renatoconrado.libraryapi.authors.controller;

import io.github.renatoconrado.libraryapi.authors.controller.mapper.AuthorMapper;
import io.github.renatoconrado.libraryapi.authors.model.Author;
import io.github.renatoconrado.libraryapi.authors.model.AuthorDTO;
import io.github.renatoconrado.libraryapi.authors.service.AuthorService;
import io.github.renatoconrado.libraryapi.common.GenericController;
import io.github.renatoconrado.libraryapi.exception.custom.InvalidFieldsException;
import io.github.renatoconrado.libraryapi.exception.custom.ProcedureNotAllowedException;
import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authors")
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
        List<AuthorDTO> authorDTOs = authorService
            .queryByExample(name, citizenship)
            .stream()
            .map(AuthorDTO::of)
            .toList();
        if (authorDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorDTOs);
    }

    @Operation(summary = "Create", description = "Create new Author")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created with success"),
        @ApiResponse(responseCode = "422", description = "Validation Error"),
        @ApiResponse(responseCode = "409", description = "Author already exist")
    })
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid AuthorDTO dto) {
        log.info("Creating new Author {}", dto.name());

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
        User user = userService.getByUsername(userDetails.getUsername()).orElseThrow();

        Author author = mapper.toEntity(dto);
        author.setUser(user);

        authorService.create(author);
        return ResponseEntity.created(generateHeaderURI(author.getId())).build();
    }

    @Operation(summary = "Get Details", description = "Receive a Detailed ")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Author received"),
        @ApiResponse(responseCode = "404", description = "Author was not Found")
    })
    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable UUID id) {
        return authorService
            .getById(id)
            .map(mapper::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * @throws InvalidFieldsException if all fields are empty
     */
    @Operation(summary = "Update", description = "Update an Author")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Updated successfully"),
        @ApiResponse(responseCode = "404", description = "Author not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input: empty fields")
    })
    @PutMapping("{id}")
    public ResponseEntity<Void> update(
        @PathVariable UUID id,
        @RequestBody @Valid AuthorDTO authorDTO
    ) {
        boolean updated = authorService.update(id, authorDTO);
        if (updated) {
            log.info("Updated: Author {}", authorDTO.id());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /** @throws ProcedureNotAllowedException if Author has Books */
    @Operation(summary = "Delete", description = "Delete an Author")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Delete with success"),
        @ApiResponse(responseCode = "404", description = "Author was Not Found"),
        @ApiResponse(responseCode = "400", description = "Author has a book")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean authorWasDeleted = authorService.delete(id);
        if (authorWasDeleted) {
            log.info("Deleted: Author {}", id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
