package io.github.renatoconrado.libraryapi.client.service;

import io.github.renatoconrado.libraryapi.client.model.Client;
import io.github.renatoconrado.libraryapi.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientService {
    private final ClientRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Client save(Client client) {
        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
        return repository.save(client);
    }

    public Optional<Client> getByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }
}
