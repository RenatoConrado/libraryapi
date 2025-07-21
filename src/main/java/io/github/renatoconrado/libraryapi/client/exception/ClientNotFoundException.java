package io.github.renatoconrado.libraryapi.client.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String clientId) {
        super("Client with ID '" + clientId + "' not found");
    }
}
