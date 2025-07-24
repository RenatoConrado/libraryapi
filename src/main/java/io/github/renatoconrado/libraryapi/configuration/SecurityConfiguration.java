package io.github.renatoconrado.libraryapi.configuration;

import io.github.renatoconrado.libraryapi.configuration.authentication.JwtCustomAuthenticationFilter;
import io.github.renatoconrado.libraryapi.configuration.authentication.LoginSocialSuccessHandler;
import io.github.renatoconrado.libraryapi.users.service.CustomUserDetailsService;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import static io.github.renatoconrado.libraryapi.configuration.Constants.Endpoints;
import static io.github.renatoconrado.libraryapi.configuration.Constants.Roles;

@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public @Configuration class SecurityConfiguration {

    @Deprecated
    public static JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        var authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChainConfiguration(
        HttpSecurity http,
        LoginSocialSuccessHandler loginSocialSuccessHandler,
        JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .httpBasic(Customizer.withDefaults())
            .formLogin(formLogin -> formLogin.loginPage(Endpoints.LOGIN))
            .authorizeHttpRequests(authorize -> {
                authorize.requestMatchers(Endpoints.PERMISSIVE).permitAll();
                authorize.requestMatchers(HttpMethod.GET, Endpoints.SENSITIVE)
                    .authenticated();
                authorize.anyRequest().hasRole(Roles.ADMIN);
            })
            .oauth2Login(authLogin ->
                authLogin.loginPage(Endpoints.LOGIN)
                    .successHandler(loginSocialSuccessHandler)
            )
            .oauth2ResourceServer(authResourceServer ->
                authResourceServer.jwt(Customizer.withDefaults())
            )
            .addFilterAfter(
                jwtCustomAuthenticationFilter,
                BearerTokenAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(Endpoints.IGNORE);
    }

}
