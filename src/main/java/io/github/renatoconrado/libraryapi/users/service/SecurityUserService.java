package io.github.renatoconrado.libraryapi.users.service;

import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.configuration.authentication.CustomAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
public @Service class SecurityUserService {

    private final UserService service;

    @Deprecated
    public Optional<User> oldGetLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return service.getByUsername(userDetails.getUsername());
    }

    public Optional<User> getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof CustomAuthentication customAuthentication) {
            return Optional.ofNullable(customAuthentication.getUser());
        }
        return Optional.empty();
    }
}
