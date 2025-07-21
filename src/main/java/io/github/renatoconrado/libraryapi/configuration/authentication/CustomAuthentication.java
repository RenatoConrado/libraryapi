package io.github.renatoconrado.libraryapi.configuration.authentication;

import io.github.renatoconrado.libraryapi.users.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomAuthentication implements Authentication {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
            .stream()
            .map(s -> "ROLE_" + s)
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * @param isAuthenticated {@code true} if the token should be trusted (which may
     * result in an exception) or {@code false} if the token should not be trusted
     * @throws IllegalArgumentException if an attempt to make the authentication token
     * trusted (by passing {@code true} as the argument) is rejected due to the
     * implementation being immutable or implementing its own alternative approach to
     * {@link #isAuthenticated()}
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) {}

    @Override
    public String getName() {
        return user.getLogin();
    }
}
