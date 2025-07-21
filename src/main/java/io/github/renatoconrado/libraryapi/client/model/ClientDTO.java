package io.github.renatoconrado.libraryapi.client.model;

import jakarta.validation.constraints.NotBlank;

public record ClientDTO(
    @NotBlank(message = "Required Field") String clientId,
    @NotBlank(message = "Required Field") String clientSecret,
    @NotBlank(message = "Required Field") String redirectUri,
    String scope
) {}
