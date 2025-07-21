package io.github.renatoconrado.libraryapi.client.security;

import io.github.renatoconrado.libraryapi.client.exception.ClientNotFoundException;
import io.github.renatoconrado.libraryapi.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService service;
    private final TokenSettings tokenSettings;
    private final ClientSettings clientSettings;

    @Override
    public void save(RegisteredClient registeredClient) {}

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    /**
     * @param clientId the client identifier
     * @return {@link RegisteredClient}
     * @throws ClientNotFoundException if entity Client was not found in the database
     */
    @Override
    public RegisteredClient findByClientId(String clientId) {
        return service.getByClientId(clientId)
            .map(client -> RegisteredClient
                .withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectUri())
                .scope(client.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build()
            )
            .orElseThrow(() -> new ClientNotFoundException(clientId));
    }
}
