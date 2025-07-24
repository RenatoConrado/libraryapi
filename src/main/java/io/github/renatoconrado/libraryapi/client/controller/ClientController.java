package io.github.renatoconrado.libraryapi.client.controller;

import io.github.renatoconrado.libraryapi.client.model.ClientDTO;
import io.github.renatoconrado.libraryapi.client.model.ClientMapper;
import io.github.renatoconrado.libraryapi.client.service.ClientService;
import io.github.renatoconrado.libraryapi.common.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("clients")
@PreAuthorize("hasRole('ADMIN')")
@RestController
public class ClientController implements GenericController {
    private final ClientService service;
    private final ClientMapper mapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ClientDTO dto) {
        UUID id = service.save(mapper.toEntity(dto)).getId();
        log.info("Created new Client: {}, with scope: {}", id, dto.scope());

        URI headerURI = generateHeaderURI(id);
        return ResponseEntity.created(headerURI).build();
    }
}
