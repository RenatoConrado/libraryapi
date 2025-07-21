package io.github.renatoconrado.libraryapi.users.controller;

import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.users.model.UserDTO;
import io.github.renatoconrado.libraryapi.users.model.UserMapper;
import io.github.renatoconrado.libraryapi.users.model.UserSafeDTO;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("users")
public @RestController class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<Page<UserSafeDTO>> query(
        @RequestParam(required = false) final String username,
        @RequestParam(required = false) final String email,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10", value = "page-size") Integer pageSize
    ) {
        return ResponseEntity.ok(
            service.query(username, email, page, pageSize).map(mapper::userToSafeDTO)
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@Valid @RequestBody UserDTO dto) {
        User user = mapper.toEntity(dto);
        service.save(user);
    }

}
