package io.github.renatoconrado.libraryapi.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService service;

    /**
     * @param username the username identifying the user whose data is required.
     * @return A Custom {@link User} with fields: login, password and roles.
     * @throws UsernameNotFoundException if the User is not in the db
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return service.getByUsername(username)
            .map(user -> User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(String[]::new))
                .build()
            )
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
