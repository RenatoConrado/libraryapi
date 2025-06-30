package io.github.renatoconrado.libraryapi.users.service;

import io.github.renatoconrado.libraryapi.users.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public @Service class SecurityUserService {

    private final UserService service;

    public Users getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return this.service.getByUsername(userDetails.getUsername());
    }
}
