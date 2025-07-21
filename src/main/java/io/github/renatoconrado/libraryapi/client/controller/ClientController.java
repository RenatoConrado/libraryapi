package io.github.renatoconrado.libraryapi.client.controller;

import io.github.renatoconrado.libraryapi.client.model.ClientDTO;
import io.github.renatoconrado.libraryapi.client.model.ClientMapper;
import io.github.renatoconrado.libraryapi.client.service.ClientService;
import io.github.renatoconrado.libraryapi.common.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("clients")
@PreAuthorize("hasRole('ADMIN')")
@RestController
public class ClientController implements GenericController {
    private final ClientService service;
    private final ClientMapper mapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ClientDTO dto) {
        UUID id = service.save(mapper.toEntity(dto)).getId();
        return ResponseEntity.created(this.generateHeaderURI(id)).build();
    }
}
