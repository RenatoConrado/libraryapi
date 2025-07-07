package io.github.renatoconrado.libraryapi.users.service;

import io.github.renatoconrado.libraryapi.security.CustomAuthentication;
import io.github.renatoconrado.libraryapi.users.model.User;
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
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return this.service.getByUsername(userDetails.getUsername());
    }

    public Optional<User> getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if(authentication instanceof CustomAuthentication customAuthentication) {
            return Optional.ofNullable(customAuthentication.getUser());
        }
        return Optional.empty();
    }
}
