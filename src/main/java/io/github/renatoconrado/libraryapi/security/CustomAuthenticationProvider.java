package io.github.renatoconrado.libraryapi.security;

import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public @Component class CustomAuthenticationProvider
    implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        User user = this.userService.getByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException("User Not Found")
        );
        boolean passwordMatch = encoder.matches(rawPassword, user.getPassword());
        if (passwordMatch) {
            return new CustomAuthentication(user);
        } else {
            throw new BadCredentialsException("Invalid Password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
