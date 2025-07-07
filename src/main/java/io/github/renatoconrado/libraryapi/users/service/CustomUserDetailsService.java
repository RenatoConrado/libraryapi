package io.github.renatoconrado.libraryapi.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        var user = this.service.getByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException(username)
        );

        return User.builder()
            .username(user.getLogin())
            .password(user.getPassword())
            .roles(user.getRoles().toArray(String[]::new))
            .build();
    }
}
