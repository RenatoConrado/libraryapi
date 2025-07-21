package io.github.renatoconrado.libraryapi.configuration.authentication;

import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public @Component class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    /**
     * @param authentication the authentication request object.
     * @return A {@link CustomAuthentication}
     * @throws UsernameNotFoundException If the {@link User} was not found
     * @throws BadCredentialsException If the given password don't match with the
     * encrypted password
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        String login = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        User dbUser = userService
            .getByUsername(login)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        String dbPassword = dbUser.getPassword();

        boolean passwordMatch = encoder.matches(rawPassword, dbPassword);
        if (!passwordMatch) {
            throw new BadCredentialsException("Invalid Password");
        }

        return new CustomAuthentication(dbUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
