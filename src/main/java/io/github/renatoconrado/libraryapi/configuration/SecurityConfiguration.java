package io.github.renatoconrado.libraryapi.configuration;

import io.github.renatoconrado.libraryapi.users.service.CustomUserDetailsService;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
public @Configuration class SecurityConfiguration {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    private static final String[] PERMISSIVE_ENDPOINTS = { "/login", "/users" };
    private static final String[] SENSITIVE_ENDPOINTS = { "/authors/**", "/books/**" };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(Customizer.withDefaults())
            .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login"))
            .authorizeHttpRequests(authorize -> {
                authorize.requestMatchers(PERMISSIVE_ENDPOINTS).permitAll();
                authorize.requestMatchers(HttpMethod.GET, SENSITIVE_ENDPOINTS).hasRole(ROLE_USER);
                authorize.anyRequest().hasRole(ROLE_ADMIN);
            })
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }

    @Deprecated
    public UserDetailsService oldUserDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User
            .builder()
            .username("user")
            .password(passwordEncoder.encode("123"))
            .roles(ROLE_USER)
            .build();

        UserDetails admin = User
            .builder()
            .username("admin")
            .password(passwordEncoder.encode("123"))
            .roles(ROLE_ADMIN)
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}
